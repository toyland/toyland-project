package com.toyland.payment.application.usecase;

import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

    PaymentResponseDto createPayment(PaymentRequestDto requestDto, Long loginUserId);
    PaymentResponseDto findByPaymentId(UUID paymentId);
    PaymentUpdateResponseDto updatePayment(PaymentUpdateRequestDto requestDto);
    void deletePayment(UUID paymentId, Long loginUserId);
    Page<PaymentSearchResponseDto> searchPayment(PaymentSearchRequestDto searchRequestDto, Pageable pageable);
}
