package com.toyland.region.model.repository;

import com.toyland.region.model.entity.Region;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface RegionRepository {

    Region save(Region region);

    Optional<Region> findById(UUID regionId);

    void deleteById(UUID regionId);

    Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable);

    //테스트
    <S extends Region> Iterable<S> saveAll(Iterable<S> entities);

}
