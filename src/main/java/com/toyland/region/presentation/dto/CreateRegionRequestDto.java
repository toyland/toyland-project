package com.toyland.region.presentation.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public record CreateRegionRequestDto(
        @NotEmpty(message = "지역 이름은 반드시 입력해야 합니다.")
        String regionName) {
}
