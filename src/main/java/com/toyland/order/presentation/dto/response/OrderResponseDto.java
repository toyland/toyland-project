package com.toyland.order.presentation.dto.response;

import com.toyland.order.model.Order;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public record OrderResponseDto(UUID orderId,
                               String orderStatus,
                               String paymentType,
                               List<OrderProductResponseDto> orderProducts) {

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOrderStatus().getDescription(),
            order.getPaymentType().getDescription(),
            order.getOrderProductList().stream()
                    .map(OrderProductResponseDto::from).collect(Collectors.toList()) // 주문 상품 리스트 변환
        );
    }
}