package com.toyland.category.application.usecase.dto;

import com.toyland.category.presentation.dto.UpdateCategoryRequestDto;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateCategoryServiceRequestDto (
    UUID categoryId, String name, UUID parentCategoryId
) {

  public static UpdateCategoryServiceRequestDto of(
      UpdateCategoryRequestDto request, UUID categoryId) {
    return UpdateCategoryServiceRequestDto.builder()
        .name(request.name())
        .categoryId(categoryId)
        .parentCategoryId(request.parentCategoryId())
        .build();
  }

  public boolean isEqualsParentCategoryId(){
    return this.categoryId.equals(this.parentCategoryId);
  }

  public List<UUID> getCategoryList(){
    return parentCategoryId == null ? List.of(categoryId) : List.of(categoryId, parentCategoryId);
  }
}
