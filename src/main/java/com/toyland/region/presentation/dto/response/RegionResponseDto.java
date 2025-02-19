package com.toyland.region.presentation.dto.response;

import com.toyland.region.model.entity.Region;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public record RegionResponseDto(UUID regionId,
                                String regionName) {

    public static RegionResponseDto from(Region region) {
        return new RegionResponseDto(
            region.getId(),
            region.getRegionName()
        );

    }
}
