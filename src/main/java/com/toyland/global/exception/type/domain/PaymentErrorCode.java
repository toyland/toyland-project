package com.toyland.global.exception.type.domain;

import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 17.
 */
@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_NOT_FOUND("PAYMENT_404", "존재 하지 않는 결제 내역입니다.", HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final HttpStatus status;
}
