package com.toyland.order.presentation.dto.request;

import java.util.UUID;

public record OrderSearchRequestDto(UUID orderId,
                                    Long userId,
                                    UUID storeId,
                                    String storeName,
                                    String orderStatus) {
}