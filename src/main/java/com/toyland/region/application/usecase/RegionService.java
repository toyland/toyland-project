package com.toyland.region.application.usecase;

import com.toyland.region.model.entity.Region;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.RegionResponseDto;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface RegionService {
    RegionResponseDto createRegion(CreateRegionRequestDto requestDto);

    Region findByRegionId(UUID regionId);
}
