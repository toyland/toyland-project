/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateProductRequestDto (
    @NotEmpty(message = "상품 이름은 필수값입니다.")
    String name,

    @NotNull(message = "상품 가격은 필수값입니다.")
    @Positive(message = "상품 가격은 0을 초과한 금액만 가능합니다.")
    BigDecimal price,

    boolean isDisplay
){

}
