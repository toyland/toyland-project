/**
 * @Date : 2025. 02. 19.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteProductServiceRequestDto
    (Long actorId, LocalDateTime eventDateTime, UUID productId){

  public static DeleteProductServiceRequestDto of(Long actorId, UUID productId,
      LocalDateTime eventDateTime) {
    return DeleteProductServiceRequestDto.builder()
        .actorId(actorId)
        .productId(productId)
        .eventDateTime(eventDateTime)
        .build();
  }
}
