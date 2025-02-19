package com.toyland.order.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.order.application.OrderService;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
