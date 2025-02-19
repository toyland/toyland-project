package com.toyland.global.exception.type.domain;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
public enum CategoryErrorCode implements ErrorCode {
    ID_DUPLICATE("CATEGORY_400", "category id들에 중복 값이 존재합니다.", BAD_REQUEST),
    CATEGORY_NOT_FOUND("CATEGORY_404","존재하지 않는 카테고리입니다. 카테고리 id를 확인해주세요.", NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
