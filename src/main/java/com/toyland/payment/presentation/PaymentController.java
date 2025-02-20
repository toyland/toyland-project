package com.toyland.payment.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.payment.application.facade.PaymentFacade;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping
    public PaymentResponseDto createPayment(
        @RequestBody PaymentRequestDto requestDto,
        @CurrentLoginUserId Long loginUserId) {
        return paymentFacade.createPayment(requestDto, loginUserId);
    }

}
