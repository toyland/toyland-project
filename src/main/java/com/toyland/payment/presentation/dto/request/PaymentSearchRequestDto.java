package com.toyland.payment.presentation.dto.request;

import com.toyland.payment.model.entity.PaymentStatus;

import java.util.UUID;

public record PaymentSearchRequestDto(UUID paymentId,
                                      PaymentStatus paymentStatus) {
}
