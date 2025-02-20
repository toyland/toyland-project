package com.toyland.payment.presentation.dto.response;

import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.entity.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Builder
public record PaymentResponseDto(UUID paymentId,
                                 UUID orderId,
                                 PaymentStatus paymentStatus) {

    public static PaymentResponseDto from(Payment payment) {
        return PaymentResponseDto.builder()
            .paymentId(payment.getId())
            .paymentStatus(payment.getPaymentStatus())
            .orderId(payment.getOrder().getId())
            .build();
    }

}
