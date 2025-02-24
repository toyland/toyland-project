package com.toyland.global.exception.type.domain;


import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    INVALID_STATUS("ORDER_400", "잘못된 주문 상태입니다.", HttpStatus.BAD_REQUEST),
    CANCEL_UPDATE_TIME_EXCEEDED("ORDER_402", "주문 생성 후 5분 이내에만 수정 및 취소할 수 있습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("ORDER_403", "본인이 생성한 주문만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),
    ORDER_NOT_FOUND("ORDER_404", "존재하지 않는 주문입니다. ID를 확인해주세요.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
