package com.toyland.order.presentation.dto.response;

import com.toyland.order.model.Order;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *  주문 목록 응답 DTO
 *    -주문 목록에서는 필요한 정보만 요약하여 제공
 *    -상세 정보는 주문 상세 조회에서 확인
 */
@Builder
public record OrderSearchResponseDto(
        UUID orderId,              // 주문 ID
        String orderStatus,        // 주문 상태 (주문완료, 주문취소, 조리중, 배달중, 배달완료)
        String orderType,          // 주문 유형 (포장, 배달)
        String paymentType,        // 결제 유형 (카드, 현금)
        LocalDateTime createdAt,   // 주문 생성일
        LocalDateTime deletedAt,   // 주문 취소일
        List<String> productNames, // 상품 이름 리스트
        String storeName           // 상점 이름
) {

    /**
     * Order -> OrderSearchResponseDto 변환 메서드
     * @param order : Order 엔티티
     * @return OrderSearchResponseDto
     */
    public static OrderSearchResponseDto from(Order order) {
        return OrderSearchResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus().name())
                .orderType(order.getOrderType().name())
                .paymentType(order.getPaymentType().name())
                .createdAt(order.getCreatedAt())
                .deletedAt(order.getDeletedAt())

                // 상품 이름 리스트 직접 가져오기
                .productNames(order.getOrderProductList().stream()
                        .map(op -> op.getProduct().getName())
                        .collect(Collectors.toList()))

                // 하나의 상점 이름만 가져오기 (첫 번째 상품의 상점 이름 사용)
                .storeName(order.getOrderProductList().stream()
                        .findFirst()  // 첫 번째 상품의 상점 이름 사용
                        .map(op -> op.getProduct().getStore().getName())
                        .orElse("N/A"))  // 상품이 없을 경우 "N/A" 반환
                .build();
    }
}
