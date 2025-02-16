package com.toyland.global.exception.type.domain;

import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
  NOT_FOUND("PRODUCT_404", "존재하지 않는 상품입니다. id를 확인해주세요.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;

}
