package com.toyland.region.application.usecase;

import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface RegionService {

    RegionResponseDto createRegion(CreateRegionRequestDto requestDto);

    RegionResponseDto findByRegionId(UUID regionId);

    RegionResponseDto updateRegion(UUID regionId, CreateRegionRequestDto requestDto);

    void deleteByRegionId(UUID regionId, Long userId);

    Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable);
}
