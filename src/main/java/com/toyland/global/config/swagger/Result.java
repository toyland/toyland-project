package com.toyland.global.config.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Getter
@RequiredArgsConstructor
public enum Result {

    Ok(0, "성공"),
    FAIL(-1, "실패");

    private final int code;
    private final String message;
}
