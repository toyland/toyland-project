package com.toyland.address.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public record CreateAddressRequestDto(
    @NotNull(message = "주소를 적어주세요.")
    String addressName,
    UUID regionId) {

}
