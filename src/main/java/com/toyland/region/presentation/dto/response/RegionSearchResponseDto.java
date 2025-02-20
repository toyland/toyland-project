package com.toyland.region.presentation.dto.response;

import com.toyland.region.model.entity.Region;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
@Builder
public record RegionSearchResponseDto(UUID regionId,
                                      String regionName,
                                      Long createdBy,
                                      LocalDateTime createdAt,
                                      Long updatedBy,
                                      LocalDateTime updatedAt,
                                      Long deletedBy,
                                      LocalDateTime deletedAt) {

    public static RegionSearchResponseDto from(Region region) {
        return RegionSearchResponseDto.builder()
            .regionId(region.getId())
            .regionName(region.getRegionName())
            .createdAt(region.getCreatedAt())
            .createdBy(region.getCreatedBy())
            .updatedAt(region.getUpdatedAt())
            .updatedBy(region.getUpdatedBy())
            .deletedAt(region.getDeletedAt())
            .deletedBy(region.getDeletedBy())
            .build();
    }


}
