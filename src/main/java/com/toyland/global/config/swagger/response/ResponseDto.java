package com.toyland.global.config.swagger.response;

import com.toyland.global.exception.type.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 21.
 */

@Getter
@RequiredArgsConstructor
public class ResponseDto {

    private final String code;
    private final String message;

    public static ResponseDto of(ApiErrorCode errorCode) {
        return new ResponseDto(errorCode.toString(), errorCode.getMessage());
    }
}
