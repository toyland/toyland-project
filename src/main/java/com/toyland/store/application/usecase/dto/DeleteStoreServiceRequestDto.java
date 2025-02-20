package com.toyland.store.application.usecase.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteStoreServiceRequestDto (
    Long actorId, LocalDateTime eventDateTime, UUID storeId){

  public static DeleteStoreServiceRequestDto of(
      Long actorId, UUID storeId, LocalDateTime eventDateTime) {
    return DeleteStoreServiceRequestDto.builder()
        .actorId(actorId)
        .storeId(storeId)
        .eventDateTime(eventDateTime)
        .build();
  }
}
