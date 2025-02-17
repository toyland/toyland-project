/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;

public record CreateStoreRequestDto(
    @NotEmpty
    String name,

    String content,

    @NotEmpty
    String address,

    UUID regionId,

    Long ownerId
) {

}
