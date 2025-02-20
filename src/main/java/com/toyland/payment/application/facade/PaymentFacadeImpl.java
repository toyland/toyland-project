package com.toyland.payment.application.facade;

import com.toyland.payment.application.usecase.PaymentService;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@RequiredArgsConstructor
@Component
public class PaymentFacadeImpl implements PaymentFacade {

    private final PaymentService paymentService;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto, Long loginUserId) {
        return paymentService.createPayment(requestDto, loginUserId);
    }

    @Override
    public PaymentUpdateResponseDto updatePayment(PaymentUpdateRequestDto requestDto) {
        return paymentService.updatePayment(requestDto);
    }

    @Override
    public void deletePayment(UUID paymentId, Long loginUserId) {
        paymentService.deletePayment(paymentId, loginUserId);
    }

}
