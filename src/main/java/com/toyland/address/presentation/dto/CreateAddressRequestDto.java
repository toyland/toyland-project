package com.toyland.address.presentation.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public record CreateAddressRequestDto(
        @NotNull(message = "주소를 적어주세요.")
        String addressName,
        //security 구성시 controller 파라미터에서 인증 객체 받아올 것이기 때문에 나중에 수정 해야함
        Long userId,
        UUID regionId) {
}
