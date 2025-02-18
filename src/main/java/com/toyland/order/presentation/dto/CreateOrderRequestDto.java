package com.toyland.order.presentation.dto;

import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.orderproduct.presentation.dto.OrderProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrderRequestDto {

    @NotEmpty(message = "주문 상품 목록은 비어 있을 수 없습니다.")
    private List<@Valid OrderProductRequestDto> orderProducts; // 주문 상품 목록

    @NotNull(message = "주문 유형은 필수입니다.")
    private OrderType orderType; // 주문 유형 (배달/포장)

    @NotNull(message = "결제 유형은 필수입니다.")
    private PaymentType paymentType; // 결제 유형 (카드/현금)
}
