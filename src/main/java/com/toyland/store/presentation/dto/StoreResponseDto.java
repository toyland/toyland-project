package com.toyland.store.presentation.dto;

import com.toyland.store.model.entity.Store;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreResponseDto (
    UUID id, String name, String content, String address, String regionName, String ownerName
){

  public static StoreResponseDto from(Store store) {
    return StoreResponseDto.builder()
        .id(store.getId())
        .name(store.getName())
        .content(store.getContent())
        .address(store.getAddress())
        .regionName(store.getRegion() == null ? null : store.getRegion().getRegionName())
        .ownerName(store.getOwner() == null ? null : store.getOwner().getUsername())
        .build();
  }
}
