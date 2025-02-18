package com.toyland.region.infrastructure;

import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public interface JpaRegionRepositoryCustom {

    Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable);

}
