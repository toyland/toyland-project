package com.toyland.global.config.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Getter
@RequiredArgsConstructor
public class CommonResponse<T> {

    private final int code;
    private final String message;
    private final T result;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(Result.Ok.getCode(), Result.Ok.getMessage(), data);
    }
}
