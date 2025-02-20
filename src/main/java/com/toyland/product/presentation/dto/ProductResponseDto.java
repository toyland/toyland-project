/**
 * @Date : 2025. 02. 19.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.dto;

import com.toyland.product.model.entity.Product;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProductResponseDto(
    UUID id, String name, BigDecimal price, boolean isDisplay) {

  public static ProductResponseDto from(Product product) {
    return ProductResponseDto.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .isDisplay(product.isDisplay())
        .build();
  }
}
