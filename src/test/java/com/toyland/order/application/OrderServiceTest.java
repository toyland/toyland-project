package com.toyland.order.application;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;


class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RegionRepository regionRepository;




    @DisplayName("주문을 생성 및 조회한다.")
    @Test
    @Transactional
    void createOrder() {
        /** given **/
        //유저 생성
        User user = userRepository.save(createUser("customer", "1234", UserRoleEnum.CUSTOMER));

        //상점 생성
        Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

        //지역 생성
        Region region = regionRepository.save(new Region("서울"));

        //주소 생성
        Address address = addressRepository.save(createdAddress("영등포구", user, region)) ;



        // 상품 생성
        Product product1 = createProduct("고추바사삭", BigDecimal.valueOf(10000), goobne);
        Product product2 = createProduct("볼케이노", BigDecimal.valueOf(20000), goobne);
        Product product3 = createProduct("오리지널", BigDecimal.valueOf(30000), goobne);
        productRepository.saveAll(List.of(product1, product2, product3));


        // 주문 상품 리스트 생성
        List<OrderProductRequestDto> orderProducts = List.of(
                new OrderProductRequestDto(product1.getId(), product1.getPrice(), 1),
                new OrderProductRequestDto(product2.getId(), product2.getPrice(), 2),
                new OrderProductRequestDto(product3.getId(), product3.getPrice(), 3)
        );

        // 주문 요청 DTO 생성
        CreateOrderRequestDto createOrderRequestDto =
                new CreateOrderRequestDto(orderProducts, OrderType.ONLINE_DELIVERY, PaymentType.CARD, address.getId(), "30동 100호", "맵게해주세요.");



        /** when **/
        // 주문 생성
        Order order = orderService.createOrder(createOrderRequestDto, user.getId());



        /** then **/
        // 저장된 주문 조회
        List<Order> savedOrders = orderRepository.findAll();


        // 주문이 1개 존재하는지 확인 & 주문 정보 검증
        assertThat(savedOrders).hasSize(1)
                .extracting("orderStatus", "orderType", "paymentType", "user.username", "address.region.regionName", "address.addressName", "addressDetail", "orderRequest")
                .containsExactlyInAnyOrder(
                        tuple(OrderStatus.ORDER_PENDING, OrderType.ONLINE_DELIVERY, PaymentType.CARD, "customer", "서울", "영등포구", "30동 100호", "맵게해주세요.")
                );


        // 주문한 상품이 3개 존재하는지 확인 & 상품 정보 검증
        assertThat(savedOrders.get(0).getOrderProductList()).hasSize(3)
                .extracting("product.name", "orderProductPrice", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("고추바사삭", BigDecimal.valueOf(10000), 1),
                        tuple("볼케이노", BigDecimal.valueOf(20000), 2),
                        tuple("오리지널", BigDecimal.valueOf(30000), 3)
                );
    }




    @DisplayName("주문을 삭제한다.")
    @Test
    @Transactional
    void deleteOrder() {
        /** given **/
        //유저 생성
        User user = userRepository.save(createUser("master", "1234", UserRoleEnum.MASTER));

        //상점 생성
        Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

        //지역 생성
        Region region = regionRepository.save(new Region("서울"));

        //주소 생성
        Address address = addressRepository.save(createdAddress("영등포구", user, region)) ;

        // 상품 생성
        Product product1 = createProduct("고추바사삭", BigDecimal.valueOf(10000), goobne);
        Product product2 = createProduct("볼케이노", BigDecimal.valueOf(20000), goobne);
        Product product3 = createProduct("오리지널", BigDecimal.valueOf(30000), goobne);
        productRepository.saveAll(List.of(product1, product2, product3));


        // 주문 상품 리스트 생성
        List<OrderProductRequestDto> orderProducts = List.of(
                new OrderProductRequestDto(product1.getId(), product1.getPrice(), 1),
                new OrderProductRequestDto(product2.getId(), product2.getPrice(), 2),
                new OrderProductRequestDto(product3.getId(), product3.getPrice(), 3)
        );

        // 주문 요청 DTO 생성
        CreateOrderRequestDto createOrderRequestDto =
                new CreateOrderRequestDto(orderProducts, OrderType.ONLINE_DELIVERY, PaymentType.CARD, address.getId(), "30동 100호", "맵게해주세요.");

        // 주문 생성
        Order order = orderService.createOrder(createOrderRequestDto, user.getId());



        /** when **/
        // 주문 삭제
        orderService.deleteOrder(order.getId(), user.getId());



        /** then **/
        // 삭제 처리된 주문 조회 (논리 삭제: deleted_at IS NOT NULL)
        Optional<Order> deletedOrder = orderRepository.findById(order.getId());

        // 삭제 처리된 주문이 여전히 물리적으로는 존재하는 확인
        assertThat(deletedOrder).isPresent();

        // 삭제 처리된 주문이 논리적으로 삭제되었는지 확인
        assertThat(deletedOrder.get().getDeletedAt()).isNotNull();

        // 삭제 처리된 주문에 속한 OrderProduct도 논리 삭제되었는지 확인
        List<OrderProduct> deletedOrderProducts = deletedOrder.get().getOrderProductList();
        assertThat(deletedOrderProducts).hasSize(3)
                .allMatch(orderProduct -> orderProduct.getDeletedAt() != null);
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


    private Address createdAddress(String addressName, User user, Region region) {
        return Address.builder()
                .addressName(addressName)
                .user(user)
                .region(region)
                .build();
    }


}