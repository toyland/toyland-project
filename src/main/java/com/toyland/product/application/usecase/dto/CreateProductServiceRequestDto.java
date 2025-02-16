package com.toyland.product.application.usecase.dto;

import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import com.toyland.store.model.entity.Store;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CreateProductServiceRequestDto(
    String name,

    BigDecimal price,

    boolean isDisplay,

    Store store
) {
  public static CreateProductServiceRequestDto of(CreateProductRequestDto dto, Store store) {
    return CreateProductServiceRequestDto.builder()
        .name(dto.name())
        .price(dto.price())
        .isDisplay(dto.isDisplay())
        .store(store)
        .build();
  }
}
