package com.toyland.order.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.order.application.OrderService;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderResponseDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequestDto request,
                                            @CurrentLoginUserId Long loginUserId) {
        orderService.createOrder(request, loginUserId);
        return ResponseEntity.ok().build();
    }



    /**
     * 주문 수정 (주문 사항 변경)
     * @param orderId 주문 번호
     * @return 200 성공
     */
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @PutMapping("/{orderId}/menu")
    public ResponseEntity<OrderResponseDto> updateOrderByOrderId(@PathVariable UUID orderId,
                                                                 @RequestBody CreateOrderRequestDto requestDto,
                                                                 @CurrentLoginUserId Long loginUserId) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, requestDto, loginUserId));
    }



    /**
     * 주문 수정 (주문 처리)
     * @param orderId 주문 번호
     * @param status 처리할 주문 상태
     * @return 200 성공
     */
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable UUID orderId,
                                                  @RequestParam OrderStatus status,
                                                  @CurrentLoginUserId Long loginUserId) {
        orderService.updateOrderStatus(orderId, status, loginUserId);
        return ResponseEntity.ok().build();
    }



    /**
     * 주문 조회(단 건 조회)
     * @param orderId
     * @return 주문 정보(OrderResponseDto)를 담은 HTTP 응답
     */
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrderByOrderId(@PathVariable UUID orderId,
                                                               @CurrentLoginUserId Long loginUserId) {
        System.out.println("loginUserId:" + loginUserId);
        return ResponseEntity.ok(orderService.findByOrderId(orderId, loginUserId));
    }



    /**
     * 주문 삭제
     * @param orderId 주문 번호
     * @return 200 성공
     */
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId,
                                            @CurrentLoginUserId Long loginUserId) {
        orderService.deleteOrder(orderId, loginUserId);
        return ResponseEntity.ok().build();
    }


    /**
     *  주문 검색
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    public Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto,
                                                    Pageable pageable,
                                                    @CurrentLoginUserId Long loginUserId) {
        return orderService.searchOrder(searchRequestDto, pageable, loginUserId);
    }

}
