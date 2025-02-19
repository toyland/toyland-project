package com.toyland.order.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.order.application.OrderService;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.createOrder(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }



    /**
     * 주문 삭제(취소)
     * @param orderId 주문 번호
     * @return 200 성공
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.deleteOrder(orderId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
