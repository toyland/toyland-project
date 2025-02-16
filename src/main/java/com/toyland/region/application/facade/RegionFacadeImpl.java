package com.toyland.region.application.facade;

import com.toyland.region.application.usecase.RegionService;
import com.toyland.region.model.entity.Region;
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
    public Region findByRegionId(UUID regionId) {
        return regionService.findByRegionId(regionId);
    }
}
