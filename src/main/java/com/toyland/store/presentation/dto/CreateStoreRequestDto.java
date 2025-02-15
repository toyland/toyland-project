/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateStoreRequestDto(
    @NotEmpty String name,
    String content,
    @NotEmpty String address
) {

}
