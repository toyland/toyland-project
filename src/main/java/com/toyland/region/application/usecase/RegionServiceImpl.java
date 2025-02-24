package com.toyland.region.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.RegionErrorCode;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.region.model.repository.command.SearchRegionRepositoryCommand;
import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public RegionResponseDto createRegion(CreateRegionRequestDto requestDto) {
        Region savedRegion = regionRepository.save(Region.from(requestDto));
        return RegionResponseDto.from(savedRegion);
    }

    @Override
    @Transactional(readOnly = true)
    public RegionResponseDto findByRegionId(UUID regionId) {
        return RegionResponseDto.from(regionRepository.findById(regionId)
            .orElseThrow(() ->
                CustomException.from(RegionErrorCode.REGION_NOT_FOUND)));

    }

    @Override
    public RegionResponseDto updateRegion(UUID regionId, CreateRegionRequestDto requestDto) {
        Region findRegion = regionRepository.findById(regionId)
            .orElseThrow(() ->
                CustomException.from(RegionErrorCode.REGION_NOT_FOUND));
        findRegion.updateRegion(requestDto.regionName());
        return RegionResponseDto.from(findRegion);
    }

    @Override
    public void deleteByRegionId(UUID regionId, Long userId) {
        Region findRegion = regionRepository.findById(regionId)
            .orElseThrow(() ->
                CustomException.from(RegionErrorCode.REGION_NOT_FOUND));
        findRegion.addDeletedField(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto) {
        return regionRepository.searchRegion(
            SearchRegionRepositoryCommand.builder()
                .regionName(searchRequestDto.regionName())
                .size(Set.of(10, 30, 50).contains(searchRequestDto.size()) ? searchRequestDto.size()
                    : 10)
                .page(searchRequestDto.page())
                .sort(searchRequestDto.sort())
                .build()
        );
    }

}
