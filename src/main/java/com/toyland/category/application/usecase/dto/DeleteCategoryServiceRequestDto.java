package com.toyland.category.application.usecase.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteCategoryServiceRequestDto (
    Long actorId, UUID categoryId, LocalDateTime eventTime) {

  public static DeleteCategoryServiceRequestDto of(
      Long actorId, UUID categoryId, LocalDateTime eventTime) {
    return DeleteCategoryServiceRequestDto.builder()
        .actorId(actorId)
        .categoryId(categoryId)
        .eventTime(eventTime)
        .build();
  }
}
