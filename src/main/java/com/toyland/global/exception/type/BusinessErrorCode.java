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
public enum BusinessErrorCode implements ErrorCode{
    //각 에러처리에 따른 Enum 필드 추가해야함. (더 좋은 방법 있는지 찾아보도록 하겠음)
    USER_NOT_FOUND("BUSINESS_001", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("BUSINESS_002", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("BUSINESS_003","존재하지 않는 카테고리입니다. 카테고리 id를 확인해주세요.", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_FOUND("BUSINESS_004","존재하지 않는 주소 입니다. 올바른 주소 ID를 입력해주세요.", HttpStatus.NOT_FOUND),
    REGION_NOT_FOUND("BUSINESS_005","존재하지 않는 지역 입니다. 올바른 지역 ID를 입력해주세요.", HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final HttpStatus status;

    public CustomException toException() {
        return new CustomException(this);
    }
}
