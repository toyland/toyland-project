package com.toyland.orderproduct.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private UUID productId;  // 상품 ID

    @NotNull(message = "가격은 필수입니다.")
    private BigDecimal price; // 상품 가격

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Integer quantity; // 주문 수량
}
