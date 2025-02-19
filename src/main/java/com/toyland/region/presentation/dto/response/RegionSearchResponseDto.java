package com.toyland.region.presentation.dto.response;

import com.toyland.region.model.entity.Region;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
// store address 추가하기
public record RegionSearchResponseDto(UUID regionId,
                                      String regionName,
                                      Long createdBy,
                                      LocalDateTime createdAt,
                                      Long updatedBy,
                                      LocalDateTime updatedAt,
                                      Long deletedBy,
                                      LocalDateTime deletedAt) {

    public static RegionSearchResponseDto from(Region region) {
        return new RegionSearchResponseDto(
            region.getId(),
            region.getRegionName(),
            region.getCreatedBy(),
            region.getCreatedAt(),
            region.getUpdatedBy(),
            region.getUpdatedAt(),
            region.getDeletedBy(),
            region.getDeletedAt()
        );
    }


}
