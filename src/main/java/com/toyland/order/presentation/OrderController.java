package com.toyland.order.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.order.application.OrderService;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderResponseDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;


    @Operation(summary = "주문 생성", description = "주문을 생성하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<OrderResponseDto>> createOrder(@Valid @RequestBody CreateOrderRequestDto request,
                                            @CurrentLoginUserId Long loginUserId) {
        OrderResponseDto order = OrderResponseDto.from(orderService.createOrder(request, loginUserId));

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/{orderId}")
                .buildAndExpand(order.orderId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(CustomApiResponse.of(HttpSuccessCode.ORDER_CREATE, order));
    }



    @Operation(summary = "주문 수정(주문 사항 변경)", description = "주문 사항을 변경하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 사항 변경 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @PutMapping("/{orderId}/menu")
    public ResponseEntity<CustomApiResponse<OrderResponseDto>> updateOrderByOrderId(@PathVariable UUID orderId,
                                                                 @RequestBody CreateOrderRequestDto requestDto,
                                                                 @CurrentLoginUserId Long loginUserId) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.ORDER_UPDATE,
                    orderService.updateOrder(orderId, requestDto, loginUserId)));
    }




    @Operation(summary = "주문 수정(주문 처리)", description = "주문을 접수하거나 취소하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 처리 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<CustomApiResponse<Void>> updateOrderStatus(@PathVariable UUID orderId,
                                                  @RequestParam OrderStatus status,
                                                  @CurrentLoginUserId Long loginUserId) {
        orderService.updateOrderStatus(orderId, status, loginUserId);
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "주문 취소", description = "주문을 취소하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MASTER', 'MANAGER')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<CustomApiResponse<Void>> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }




    @Operation(summary = "주문 단 건 조회", description = "주문 단 건 조회 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<CustomApiResponse<OrderResponseDto>> findOrderByOrderId(@PathVariable UUID orderId,
                                                               @CurrentLoginUserId Long loginUserId) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.ORDER_FIND_ONE,
                        orderService.findByOrderId(orderId, loginUserId)));
    }



    @Operation(summary = "주문 삭제", description = "주문 삭제 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주문 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')") //<Void>
    ResponseEntity<CustomApiResponse<URI>> deleteOrder(@PathVariable UUID orderId,
                                            @CurrentLoginUserId Long loginUserId) {
        orderService.deleteOrder(orderId, loginUserId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/orders")
                .build()
                .toUri();
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.ORDER_DELETE, uri));
    }


    @Operation(summary = "주문 검색", description = "주문 검색 api 입니다." +
            "예시 http://localhost:8080/api/v1/orders/search?page=0&size=10&sort=createdAt,asc" +
            "예시 http://localhost:8080/api/v1/orders/search?orderStatus=ORDER_CANCELED&page=0&size=10&sort=createdAt,asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER', 'MANAGER', 'OWNER')")
    public ResponseEntity<CustomApiResponse<Page<OrderSearchResponseDto>>> searchOrder(OrderSearchRequestDto searchRequestDto,
                                                    Pageable pageable,
                                                    @CurrentLoginUserId Long loginUserId) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.ORDER_SEARCH,
                        orderService.searchOrder(searchRequestDto, pageable, loginUserId)));
    }

}
