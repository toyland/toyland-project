package com.toyland.payment.application.facade;

import com.toyland.payment.application.usecase.PaymentService;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
