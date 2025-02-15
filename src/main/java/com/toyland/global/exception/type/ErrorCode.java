package com.toyland.global.exception.type;

import org.springframework.http.HttpStatus;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface ErrorCode {
    String getCode();
    String getMessage();
    HttpStatus getStatus();

    default int getHttpStatus() {
        return getStatus().value();
    }

}
