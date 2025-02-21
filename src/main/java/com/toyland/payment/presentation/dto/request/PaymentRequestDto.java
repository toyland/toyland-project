package com.toyland.payment.presentation.dto.request;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Builder
public record PaymentRequestDto(UUID orderId,
                                BigDecimal totalPrice) {

    public static PaymentRequestDto from(UUID orderId,
                                         BigDecimal totalPrice) {
        return new PaymentRequestDto(
                orderId,
                totalPrice);
    }
}
