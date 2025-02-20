package com.toyland.payment.presentation.dto.request;

import com.toyland.payment.model.entity.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Builder
public record PaymentRequestDto(UUID orderId,
                                PaymentStatus paymentStatus) {

}
