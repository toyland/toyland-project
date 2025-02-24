package com.toyland.region.infrastructure;

import com.toyland.region.model.repository.command.SearchRegionRepositoryCommand;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import org.springframework.data.domain.Page;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public interface JpaRegionRepositoryCustom {

    Page<RegionSearchResponseDto> searchRegion(SearchRegionRepositoryCommand build);

}
