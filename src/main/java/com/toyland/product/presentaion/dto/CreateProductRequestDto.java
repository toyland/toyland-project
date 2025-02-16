package com.toyland.product.presentaion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequestDto(
    @NotEmpty(message = "상품 이름은 필수값입니다.")
    String name,

    @NotNull(message = "상품 가격은 필수값입니다.")
    @Positive(message = "상품 가격은 0을 초과한 금액만 가능합니다.")
    BigDecimal price,

    boolean isDisplay,

    @NotNull(message = "상점 id는 필수값입니다.")
    UUID storeId
) {

}
