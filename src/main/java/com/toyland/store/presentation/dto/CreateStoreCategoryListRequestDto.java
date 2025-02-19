package com.toyland.store.presentation.dto;

import java.util.List;
import java.util.UUID;

public record CreateStoreCategoryListRequestDto(
    List<UUID> categoryIdList
) {

}
