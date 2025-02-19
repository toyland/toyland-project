package com.toyland.order.application;

import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public Order createOrder(CreateOrderRequestDto createOrderRequestDto, String username) {

        // 회원 조회
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다"));


        // 주문 상품 생성 로직
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (OrderProductRequestDto orderProductRequest : createOrderRequestDto.getOrderProducts()) {
            System.out.println("ㅡㅡㅡ  상품 조회  ㅡㅡㅡ");
            // 상품 조회
            Product product = productRepository.findById(orderProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다: " + orderProductRequest.getProductId()));

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
     * 주문 삭제(취소)
     */
    @Transactional
    public void deleteOrder(UUID orderId, String username) {

        // 회원 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다"));


        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(" 해당 주문을 찾을 수 없습니다"));


        //주문 취소
        order.cancel();
    }

}
