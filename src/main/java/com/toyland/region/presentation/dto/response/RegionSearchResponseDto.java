package com.toyland.region.presentation.dto.response;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public record RegionSearchResponseDto(UUID regionId,
                                      String regionName,
                                      UUID storeId,
                                      String storeName,
                                      String content) {


}
