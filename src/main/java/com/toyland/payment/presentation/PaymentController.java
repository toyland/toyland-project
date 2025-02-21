package com.toyland.payment.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.payment.application.facade.PaymentFacade;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<PaymentResponseDto> createPayment(
        @RequestBody PaymentRequestDto requestDto,
        @CurrentLoginUserId Long loginUserId) {
        PaymentResponseDto payment = paymentFacade.createPayment(requestDto, loginUserId);
        return ResponseEntity.ok().body(payment);
    }



    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> findPaymentByPaymentId(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentFacade.findByPaymentId(paymentId));
    }


    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable UUID paymentId,
            @CurrentLoginUserId Long loginUserId) {
            paymentFacade.deletePayment(paymentId, loginUserId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentUpdateResponseDto> updatePayment(
            @RequestBody PaymentUpdateRequestDto requestDto) {
        PaymentUpdateResponseDto responseDto = paymentFacade.updatePayment(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
