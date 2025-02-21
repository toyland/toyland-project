package com.toyland.payment.application.usecase;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.order.application.OrderService;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.entity.PaymentStatus;
import com.toyland.payment.model.repository.PaymentRepository;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Autowired
    PaymentRepository paymentRepository;


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
            OrderType.DELIVERY, PaymentType.CASH);

        Order order = orderService.createOrder(createOrderRequestDto, user.getId());

        //when
        PaymentResponseDto payment = paymentService.createPayment(
            PaymentRequestDto.builder()
                    .orderId(order.getId())
                    .build(), user.getId());

        //then
        assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.PRE_PAYMENT);
        assertThat(payment).isNotNull();
    }


    @DisplayName("결제 단 건을 조회합니다.")
    @Test
    void findPaymentByPaymentId() {
        //given
        User user = userRepository.save(createUser("admin", "1234", UserRoleEnum.MANAGER));

        // 상점 생성
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
                OrderType.DELIVERY, PaymentType.CASH);

        Order order = orderService.createOrder(createOrderRequestDto, user.getId());

        PaymentResponseDto payment = paymentService.createPayment(
                PaymentRequestDto.builder()
                        .orderId(order.getId())
                        .build(), user.getId());

        //when
        PaymentResponseDto foundPayment = paymentService.findByPaymentId(payment.paymentId());

        //then
        assertThat(foundPayment).isNotNull();
        assertThat(foundPayment.paymentId()).isEqualTo(payment.paymentId());
        assertThat(foundPayment.orderId()).isEqualTo(order.getId());
        assertThat(foundPayment.paymentStatus()).isEqualTo(PaymentStatus.PRE_PAYMENT);
    }



    @DisplayName("결제를 수정합니다.")
    @Test
    @Transactional
    void updatePayment() {
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
                OrderType.DELIVERY, PaymentType.CASH);

        Order order = orderService.createOrder(createOrderRequestDto, user.getId());

        PaymentResponseDto payment = paymentService.createPayment(
                PaymentRequestDto.builder()
                        .orderId(order.getId())
                        .build(), user.getId());



        //when
        PaymentUpdateResponseDto updatedpayment = paymentService.updatePayment(
                PaymentUpdateRequestDto.builder()
                        .id(payment.paymentId())
                        .paymentStatus(PaymentStatus.COMPLETED)
                        .orderId(order.getId())
                        .build());

        Payment updatedPayment = paymentRepository.findById(payment.paymentId()).orElse(null);

        //then

        assertEquals("COMPLETED", updatedPayment.getPaymentStatus().getDescription());
    }

    @DisplayName("결제를 삭제합니다.")
    @Test
    @Transactional
    void deletePayment() {
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
                OrderType.DELIVERY, PaymentType.CASH);

        Order order = orderService.createOrder(createOrderRequestDto, user.getId());


        PaymentResponseDto payment = paymentService.createPayment(
                PaymentRequestDto.builder()
                        .orderId(order.getId())
                        .build(), user.getId());

        //when
        paymentService.deletePayment(payment.paymentId(), user.getId());

        Payment deletedPayment = paymentRepository.findById(payment.paymentId()).orElse(null);

        System.out.println(deletedPayment.getDeletedBy());
        //then

        assertNotNull(deletedPayment);
        assertNotNull(deletedPayment.getDeletedAt());
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