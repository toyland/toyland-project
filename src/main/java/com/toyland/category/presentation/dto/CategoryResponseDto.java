package com.toyland.category.presentation.dto;

import com.toyland.category.model.entity.Category;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CategoryResponseDto (
    UUID id, String name, UUID parentCategoryId
){

  public static CategoryResponseDto from(Category category) {
    return CategoryResponseDto.builder()
        .id(category.getId())
        .name(category.getName())
        .parentCategoryId(category.getParent() == null ? null : category.getParent().getId())
        .build();
  }

}
