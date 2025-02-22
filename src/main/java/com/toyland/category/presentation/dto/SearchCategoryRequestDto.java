package com.toyland.category.presentation.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SearchCategoryRequestDto (
    String searchText, UUID parentCategoryId, Integer page, Integer size, List<String> sort
){

}
