package com.toyland.region.application.facade;

import com.toyland.region.application.usecase.RegionService;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.RegionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Component
@RequiredArgsConstructor
public class RegionFacadeImpl implements RegionFacade{

    private final RegionService regionService;

    @Override
    public RegionResponseDto createRegion(CreateRegionRequestDto requestDto) {
        return regionService.createRegion(requestDto);
    }

    @Override
    public RegionResponseDto findByRegionId(UUID regionId) {
        return regionService.findByRegionId(regionId);
    }

    @Override
    public RegionResponseDto updateRegion(UUID regionId, CreateRegionRequestDto requestDto) {
        return regionService.updateRegion(regionId, requestDto);
    }

    @Override
    public void deleteByRegionId(UUID regionId, Long userId) {
        regionService.deleteByRegionId(regionId, userId);
    }
}
