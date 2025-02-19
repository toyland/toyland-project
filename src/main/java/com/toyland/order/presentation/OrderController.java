package com.toyland.order.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.order.application.OrderService;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     *
     * @param request 주문 생성 정보
     * @return 200 성공
     */
    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequestDto request,
                                            @CurrentLoginUserId Long loginUserId) {
        orderService.createOrder(request, loginUserId);
        return ResponseEntity.ok().build();
    }



    /**
     * 주문 삭제(취소)
     * @param orderId 주문 번호
     * @return 200 성공
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId,
                                            @CurrentLoginUserId Long loginUserId) {
        orderService.deleteOrder(orderId, loginUserId);
        return ResponseEntity.ok().build();
    }
}
