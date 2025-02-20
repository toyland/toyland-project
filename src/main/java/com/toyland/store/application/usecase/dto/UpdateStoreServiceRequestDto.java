package com.toyland.store.application.usecase.dto;

import com.toyland.store.presentation.dto.UpdateStoreRequestDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateStoreServiceRequestDto (
    UUID id, String name, String content, String address, UUID regionId, Long ownerId
){

  public static UpdateStoreServiceRequestDto of(
      UpdateStoreRequestDto request, UUID storeId) {
    return UpdateStoreServiceRequestDto.builder()
        .id(storeId)
        .name(request.name())
        .content(request.content())
        .address(request.address())
        .regionId(request.regionId())
        .ownerId(request.ownerId())
        .build();
  }
}
