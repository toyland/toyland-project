package com.toyland.region.model.repository;

import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.command.SearchRegionRepositoryCommand;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface RegionRepository {

    Region save(Region region);

    Optional<Region> findById(UUID regionId);

    void deleteById(UUID regionId);

    Page<RegionSearchResponseDto> searchRegion(SearchRegionRepositoryCommand build);

    //테스트
    <S extends Region> Iterable<S> saveAll(Iterable<S> entities);

}
