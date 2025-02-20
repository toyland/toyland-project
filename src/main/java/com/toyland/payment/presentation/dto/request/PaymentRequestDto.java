package com.toyland.payment.presentation.dto.request;

import com.toyland.order.model.PaymentType;
import java.util.UUID;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Builder
public record PaymentRequestDto(UUID orderId,
                                PaymentType paymentType) {


}
