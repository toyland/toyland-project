/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase.dto;

import com.toyland.product.presentation.dto.UpdateProductRequestDto;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateProductServiceRequestDto(
    UUID id, String name, BigDecimal price, boolean isDisplay
) {

    public static UpdateProductServiceRequestDto of(UpdateProductRequestDto request,
        UUID productId) {
        return UpdateProductServiceRequestDto.builder()
            .id(productId)
            .name(request.name())
            .price(request.price())
            .isDisplay(request.isDisplay())
            .build();
    }
}
