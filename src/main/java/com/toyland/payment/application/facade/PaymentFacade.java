package com.toyland.payment.application.facade;

import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface PaymentFacade {

    PaymentResponseDto createPayment(PaymentRequestDto requestDto, Long loginUserId);


}
