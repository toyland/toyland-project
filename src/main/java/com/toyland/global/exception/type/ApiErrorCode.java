package com.toyland.global.exception.type;

import com.toyland.global.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Getter
@RequiredArgsConstructor
public enum ApiErrorCode implements ErrorCode{
    INVALID_REQUEST("API_400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("API_401", "인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("API_403", "접근이 금지 되었습니다..", HttpStatus.FORBIDDEN),
    NOT_FOUND("API_404", "요청한 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("API_500", "서버 에러 입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED("API_501", "요청한 URI 페이지는 없습니다.", HttpStatus.NOT_IMPLEMENTED),
    BAD_GATEWAY("API_502", "서버 간 통신이 올바르지 않습니다.", HttpStatus.BAD_GATEWAY);


    private final String code;
    private final String message;
    private final HttpStatus status;

    public CustomException toException() {
        return new CustomException(this);
    }

}
