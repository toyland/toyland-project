package com.toyland.order.presentation.dto.request;

import com.toyland.order.model.OrderStatus;

import java.util.UUID;

public record OrderSearchRequestDto(UUID orderId,
                                    Long userId,
                                    UUID storeId,
                                    String storeName,
                                    OrderStatus orderStatus) {
}