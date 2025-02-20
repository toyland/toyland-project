/**
 * @author : khloe
 * @Date : 2025. 02. 19.
 */
package com.toyland.global.exception.type.domain;

import com.toyland.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
  STORE_NOT_FOUND("STORE_404", "존재하지 않는 음식점입니다. 음식점 id를 확인해주세요.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
