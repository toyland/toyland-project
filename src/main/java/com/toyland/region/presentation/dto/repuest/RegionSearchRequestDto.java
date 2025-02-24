package com.toyland.region.presentation.dto.repuest;

import java.util.List;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public record RegionSearchRequestDto(String regionName,
                                     Integer page,
                                     Integer size,
                                     List<String> sort) {

}
