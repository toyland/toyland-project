package com.toyland.order.presentation.dto.request;

import java.util.UUID;

public record OrderSearchRequestDto(UUID orderId,
                                    Long userId,
                                    String orderStatus) {
}