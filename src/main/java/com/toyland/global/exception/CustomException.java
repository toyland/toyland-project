package com.toyland.global.exception;

import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }

}
