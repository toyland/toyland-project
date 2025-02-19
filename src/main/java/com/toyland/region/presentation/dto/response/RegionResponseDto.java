package com.toyland.region.presentation.dto.response;

import com.toyland.region.model.entity.Region;
import java.util.UUID;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Builder
public record RegionResponseDto(UUID regionId,
                                String regionName) {

    public static RegionResponseDto from(Region region) {
        return RegionResponseDto.builder()
            .regionId(region.getId())
            .regionName(region.getRegionName())
            .build();

    }
}
