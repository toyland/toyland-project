package com.toyland.payment.presentation.dto.response;

import com.toyland.order.model.PaymentType;
import com.toyland.payment.model.entity.Payment;
import java.util.UUID;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Builder
public record PaymentResponseDto(UUID paymentId,
                                 UUID orderId,
                                 PaymentType paymentType) {

    public static PaymentResponseDto from(Payment payment) {
        return PaymentResponseDto.builder()
            .paymentId(payment.getId())
            .paymentType(payment.getPaymentType())
            .orderId(payment.getOrder().getId())
            .build();
    }

}
