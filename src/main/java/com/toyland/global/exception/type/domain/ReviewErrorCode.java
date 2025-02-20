package com.toyland.global.exception.type.domain;

import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : khloe
 * @Date : 2025. 02. 19.
 */
@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
  REVIEW_NOT_FOUND("REVIEW_404", "존재하지 않는 리뷰입니다. 리뷰 id를 확인해주세요.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
