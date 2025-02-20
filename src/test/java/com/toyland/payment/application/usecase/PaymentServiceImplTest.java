package com.toyland.payment.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.order.application.OrderService;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
class PaymentServiceImplTest extends IntegrationTestSupport {


    @Autowired
    private PaymentService paymentService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;


    @DisplayName("결제를 생성합니다.")
    @Test
    void createPayment() {
        //given
        User user = userRepository.save(createUser("admin", "1234", UserRoleEnum.MANAGER));

        //상점 생성
        Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

        // 상품 생성
        Product product1 = createProduct("고추바사삭", BigDecimal.valueOf(10000), goobne);
        Product product2 = createProduct("볼케이노", BigDecimal.valueOf(20000), goobne);
        Product product3 = createProduct("오리지널", BigDecimal.valueOf(30000), goobne);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<OrderProductRequestDto> orderProducts = List.of(
            new OrderProductRequestDto(product1.getId(), product1.getPrice(), 1),
            new OrderProductRequestDto(product2.getId(), product2.getPrice(), 2),
            new OrderProductRequestDto(product3.getId(), product3.getPrice(), 3)
        );

        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto(orderProducts,
            OrderType.DELIVERY, PaymentType.CARD);

        Order order = orderService.createOrder(createOrderRequestDto, user.getId());

        //when
        PaymentResponseDto payment = paymentService.createPayment(
            PaymentRequestDto.builder().paymentType(PaymentType.CARD).orderId(order.getId())
                .build(), user.getId());

        //then
        assertThat(payment.paymentType()).isEqualTo(PaymentType.CARD);
        assertThat(payment).isNotNull();


    }

    private User createUser(String username, String password, UserRoleEnum role) {
        return new User(username, password, role);
    }


    private Store createStore(String name, String content, String address) {
        return Store.builder()
            .name(name)
            .content(content)
            .address(address)
            .build();
    }

    private Product createProduct(String name, BigDecimal price, Store store) {
        return Product.builder()
            .name(name)
            .price(price)
            .store(store)
            .build();
    }


}