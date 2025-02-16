package com.toyland.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int httpStatus;
}
