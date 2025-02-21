package com.toyland.global.config.swagger.response;

import com.toyland.global.exception.type.ApiErrorCode;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 21.
 */
public class ErrorResponseDto extends ResponseDto {

    private ErrorResponseDto(ApiErrorCode errorCode) {
        super(errorCode.toString(), errorCode.getMessage());
    }

    private ErrorResponseDto(ApiErrorCode errorCode, Exception e) {
        super(errorCode.toString(), errorCode.getMessage());
    }

    private ErrorResponseDto(ApiErrorCode errorCode, String message) {
        super(errorCode.toString(), errorCode.getMessage() + " - " + message);
    }

    public static ErrorResponseDto from(ApiErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(ApiErrorCode errorCode, Exception e) {
        return new ErrorResponseDto(errorCode, e);
    }

    public static ErrorResponseDto of(ApiErrorCode errorCode, String message) {
        return new ErrorResponseDto(errorCode, message);
    }
}
