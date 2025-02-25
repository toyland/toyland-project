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
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_404", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_409", "중복된 사용자입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("USER_400", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
