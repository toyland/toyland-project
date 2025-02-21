package com.toyland.global.config.swagger.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Getter
@RequiredArgsConstructor
public class CustomApiResponse<T> {

    private final HttpSuccessCode httpSuccessCode;
    private final T result;

    public static <T> CustomApiResponse<T> of(HttpSuccessCode httpSuccessCode, T result) {
        return new CustomApiResponse<>(httpSuccessCode, result);
    }
}
