package com.toyland.payment.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.payment.application.facade.PaymentFacade;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;


    @Operation(summary = "결제 생성", description = "결제 생성 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 생성 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PostMapping
    public ResponseEntity<CustomApiResponse<PaymentResponseDto>> createPayment(
        @RequestBody PaymentRequestDto requestDto,
        @CurrentLoginUserId Long loginUserId) {
        PaymentResponseDto payment = paymentFacade.createPayment(requestDto, loginUserId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/{paymentId}")
                .buildAndExpand(payment.paymentId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(CustomApiResponse.of(HttpSuccessCode.PAYMENT_CREATE, payment));

    }


    @Operation(summary = "결제 단 건 조회", description = "결제 단 건 조회 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{paymentId}")
    public ResponseEntity<CustomApiResponse<PaymentResponseDto>> findPaymentByPaymentId(@PathVariable UUID paymentId) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.PAYMENT_FIND_ONE,
                        paymentFacade.findByPaymentId(paymentId)));
    }



    @Operation(summary = "결제 삭제", description = "결제 삭제 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "결제 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<CustomApiResponse<URI>> deletePayment(
            @PathVariable UUID paymentId,
            @CurrentLoginUserId Long loginUserId) {
            paymentFacade.deletePayment(paymentId, loginUserId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/payments")
                .build()
                .toUri();
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.PAYMENT_DELETE, uri));
    }

    
    @Operation(summary = "결제 수정", description = "결제 수정 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 수정 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PutMapping("/{paymentId}")
    public ResponseEntity<CustomApiResponse<PaymentUpdateResponseDto>> updatePayment(
            @RequestBody PaymentUpdateRequestDto requestDto) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.PAYMENT_UPDATE,
                        paymentFacade.updatePayment(requestDto)));
    }



    @Operation(summary = "결제 검색", description = "결제 검색 api 입니다." +
            "예시 http://localhost:8080/api/v1/payments/search?paymentStatus=PRE_PAYMENT&size=5&sort=createdAt,asc"
            +
            "예시 http://localhost:8080/api/v1/payments/search?paymentId=0a792c67-1ef6-4421-847f-fd6ea22ca90b&size=5&sort=createdAt,asc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<PaymentSearchResponseDto>>> searchPayment(PaymentSearchRequestDto searchRequestDto,
                                                        Pageable pageable) {
        return ResponseEntity
                .ok(CustomApiResponse.of(HttpSuccessCode.PAYMENT_SEARCH,
                        paymentFacade.searchPayment(searchRequestDto, pageable)));
    }

}
