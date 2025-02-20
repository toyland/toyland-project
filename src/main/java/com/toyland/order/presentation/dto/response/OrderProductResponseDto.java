package com.toyland.order.presentation.dto.response;

import com.toyland.orderproduct.model.OrderProduct;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductResponseDto(UUID productId,
                                      String productName,
                                      Integer quantity,
                                      BigDecimal orderProductPrice,
                                      String storeName,
                                      String storeContent,
                                      String storeAddress) {

    public static OrderProductResponseDto from(OrderProduct orderProduct) {
        return new OrderProductResponseDto(
                orderProduct.getProduct().getId(),
                orderProduct.getProduct().getName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderProductPrice(), // 주문 당시 가격
                orderProduct.getProduct().getStore().getName(),
                orderProduct.getProduct().getStore().getContent(),
                orderProduct.getProduct().getStore().getAddress()
        );
    }
}