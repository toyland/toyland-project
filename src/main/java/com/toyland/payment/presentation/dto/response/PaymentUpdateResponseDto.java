package com.toyland.payment.presentation.dto.response;

import com.toyland.payment.model.entity.PaymentStatus;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;

import java.util.UUID;

public record PaymentUpdateResponseDto(UUID id
        , PaymentStatus paymentStatus
        , UUID orderId) {

    public static PaymentUpdateResponseDto from(PaymentUpdateRequestDto requestDto) {
        return new PaymentUpdateResponseDto(requestDto.id()
                , requestDto.paymentStatus()
                , requestDto.orderId());
    }
}
