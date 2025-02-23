/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SearchStoreRequestDto (
    String searchText, UUID regionId, Long ownerId, Integer page, Integer size, List<String> sort
){

}