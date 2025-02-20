package com.toyland.payment.presentation.dto.request;

import com.toyland.payment.model.entity.PaymentStatus;
import lombok.Builder;

import java.util.UUID;
@Builder
public record PaymentUpdateRequestDto(UUID id
        , PaymentStatus paymentStatus
        , UUID orderId) {

}
