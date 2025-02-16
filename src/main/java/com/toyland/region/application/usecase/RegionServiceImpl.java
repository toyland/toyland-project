package com.toyland.region.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.BusinessErrorCode;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.RegionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService{

    private final RegionRepository regionRepository;
    @Override
    public RegionResponseDto createRegion(CreateRegionRequestDto requestDto) {
        Region savedRegion = regionRepository.save(Region.from(requestDto));
        return RegionResponseDto.from(savedRegion);
    }

    @Override
    @Transactional(readOnly = true)
    public Region findByRegionId(UUID regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() ->
                        new CustomException(BusinessErrorCode.REGION_NOT_FOUND));
    }
}
