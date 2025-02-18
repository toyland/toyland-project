package com.toyland.region.presentation.dto.repuest;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public record RegionSearchRequestDto(UUID regionId,
                                     String regionName,
                                     UUID storeId,
                                     String storeName) {

}
