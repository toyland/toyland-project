package com.toyland.store.application.usecase.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record CreateStoreCategoryListServiceRequestDto(
    List<UUID> categoryIdList,
    UUID storeId,
    Long actorId,
    LocalDateTime eventTime
) {
  public Set<UUID> getCategoryIdSet() {
    return this.categoryIdList.stream().collect(Collectors.toSet());
  }

  public boolean existsDuplicateCategoryId(){
    return this.categoryIdList.size() > getCategoryIdSet().size();
  }
}
