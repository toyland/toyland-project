package com.toyland.payment.presentation.dto.response;


import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.entity.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record PaymentSearchResponseDto(UUID paymentId,
                                       PaymentStatus paymentStatus,
                                       UUID orderId,
                                       LocalDateTime createdAt,
                                       Long createdBy,
                                       LocalDateTime updatedAt,
                                       Long updatedBy,
                                       LocalDateTime deletedAt,
                                       Long deletedBy) {

    public static PaymentSearchResponseDto from(Payment payment) {
        return PaymentSearchResponseDto.builder()
                .paymentId(payment.getId())
                .paymentStatus(payment.getPaymentStatus())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .createdAt(payment.getCreatedAt())
                .createdBy(payment.getCreatedBy())
                .updatedAt(payment.getUpdatedAt())
                .updatedBy(payment.getUpdatedBy())
                .deletedAt(payment.getDeletedAt())
                .deletedBy(payment.getDeletedBy())
                .build();
    }
}
