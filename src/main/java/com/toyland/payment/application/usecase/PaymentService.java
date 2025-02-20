package com.toyland.payment.application.usecase;

import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface PaymentService {

    PaymentResponseDto createPayment(PaymentRequestDto requestDto, Long loginUserId);
    PaymentResponseDto findByPaymentId(UUID paymentId);
    PaymentUpdateResponseDto updatePayment(PaymentUpdateRequestDto requestDto);
    void deletePayment(UUID paymentId, Long loginUserId);
}
