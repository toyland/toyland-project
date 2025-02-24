package com.toyland.order.application;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.*;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderResponseDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;


    /**
     * 주문 생성
     */
    public Order createOrder(CreateOrderRequestDto createOrderRequestDto, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
            .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


        // 주소 조회
        Address address = addressRepository.findById(createOrderRequestDto.getAddressId())
                .orElseThrow(() -> CustomException.from(AddressErrorCode.ADDRESS_NOT_FOUND));


        // 주문 상품 생성 로직
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (OrderProductRequestDto orderProductRequest : createOrderRequestDto.getOrderProducts()) {
            // 상품 조회
            Product product = productRepository.findById(orderProductRequest.getProductId())
                .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));

            // 주문 상품 생성
            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderProductRequest.getPrice(), orderProductRequest.getQuantity());
            orderProductList.add(orderProduct);
        }

        // 주문 생성
        Order order = Order.createOrder(user, address, createOrderRequestDto, orderProductList);

        // 주문 저장
        orderRepository.save(order);

        return order;
    }



    /**
     * 주문 조회(단 건 조회)
     */
    @Transactional(readOnly = true)
    public OrderResponseDto findByOrderId(UUID orderId, Long loginUserId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));
        return OrderResponseDto.from(order);
    }



    /**
     *  주문 수정 (주문 사항 변경)
     */
    public OrderResponseDto updateOrder(UUID orderId, CreateOrderRequestDto createOrderRequestDto, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));

        // 주소 조회
        Address address = addressRepository.findById(createOrderRequestDto.getAddressId())
                .orElseThrow(() -> CustomException.from(AddressErrorCode.ADDRESS_NOT_FOUND));

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));


        // 주문 상품 생성 로직
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (OrderProductRequestDto orderProductRequest : createOrderRequestDto.getOrderProducts()) {
            // 상품 조회
            Product product = productRepository.findById(orderProductRequest.getProductId())
                    .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));

            // 주문 상품 생성
            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderProductRequest.getPrice(), orderProductRequest.getQuantity());
            orderProductList.add(orderProduct);
        }

        // 주문 수정
        order.update(createOrderRequestDto, orderProductList, address);

        return OrderResponseDto.from(order);
    }



    /**
     * 주문 수정 (주문 처리)
     */
    public void updateOrderStatus(UUID orderId, OrderStatus orderStatus, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));


        //주문 처리
        order.process(orderStatus);
    }



    /**
     * 주문 수정 (주문 취소)
     */
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));

        //주문 취소
        order.cancel();
    }



    /**
     * 주문 삭제 (디비에서 논리적 삭제)
     */
    public void deleteOrder(UUID orderId, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));


        //주문 삭제
        order.deleteOrder(loginUserId);
    }



    /**
     *  주문 검색
     */
    @Transactional(readOnly = true)
    public Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto,
                                                    Pageable pageable,
                                                    Long loginUserId) {

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


        // 음식점 검색 권한 체크
        if(searchRequestDto.storeId() != null) {
            System.out.println("음식점");
            Store store = storeRepository.findById(searchRequestDto.storeId())
                    .orElseThrow(() -> CustomException.from(StoreErrorCode.STORE_NOT_FOUND));
            // MANAGER, MASTER 이거나 또는 OWNER이면서 OWNER ID와 음식점 ID가 일치할 때만 음식점 검색 가능
            if (!(user.getRole().equals(UserRoleEnum.MASTER) || user.getRole().equals(UserRoleEnum.MANAGER) ||
                    (user.getRole().equals(UserRoleEnum.OWNER) && loginUserId.equals(store.getOwner().getId())))) {
                throw CustomException.from(StoreErrorCode.STORE_ACCESS_DENIED);
            }

        }
        return orderRepository.searchOrder(searchRequestDto, pageable, loginUserId, user.getRole());
    }
}
