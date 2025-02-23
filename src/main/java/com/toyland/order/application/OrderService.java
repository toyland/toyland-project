package com.toyland.order.application;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.OrderErrorCode;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderResponseDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.user.model.User;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    /**
     * 주문 생성
     */
    @Transactional
    public Order createOrder(CreateOrderRequestDto createOrderRequestDto, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
            .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


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
        Order order = Order.createOrder(user, createOrderRequestDto, orderProductList);

        // 주문 저장
        orderRepository.save(order);

        return order;
    }



    /**
     * 주문 조회(단 건 조회)
     */
    public OrderResponseDto findByOrderId(UUID orderId, Long loginUserId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));
        return OrderResponseDto.from(order);
    }



    /**
     *  주문 수정
     */
    @Transactional
    public OrderResponseDto updateOrder(UUID orderId, CreateOrderRequestDto createOrderRequestDto, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


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
        order.update(user, createOrderRequestDto, orderProductList);

        return OrderResponseDto.from(order);
    }



    /**
     * 주문 삭제(취소)
     */
    @Transactional
    public void deleteOrder(UUID orderId, Long loginUserId) {

        // 회원 조회
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));


        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));


        //주문 취소
        order.cancel();
    }

    /**
     *  주문 검색
     */
    @Transactional(readOnly = true)
    public Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto,
                                                    Pageable pageable) {
        return orderRepository.searchOrder(searchRequestDto, pageable);
    }
}
