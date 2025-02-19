package com.toyland.region.presentation.dto.repuest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public record CreateRegionRequestDto(
    @Size(max = 100, message = "지역 이름은 100자 이하로 입력해야 합니다.")
    @NotEmpty(message = "지역 이름은 반드시 입력해야 합니다.")
    String regionName) {

}
