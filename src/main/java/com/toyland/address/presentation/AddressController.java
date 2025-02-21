package com.toyland.address.presentation;

import com.toyland.address.application.facade.AddressFacade;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressFacade addressFacade;


    @Operation(summary = "주소 등록", description = "주소 등록 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주소 등록 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PostMapping
    public ResponseEntity<CustomApiResponse<AddressResponseDto>> createAddress(
        @Valid @RequestBody CreateAddressRequestDto dto,
        @CurrentLoginUserId Long userId) {
        AddressResponseDto address = addressFacade.createAddress(dto, userId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/addresses/{addressId}")
            .buildAndExpand(address.addressId())
            .toUri();
        return ResponseEntity
            .created(uri)
            .body(CustomApiResponse.of(HttpSuccessCode.ADDRESS_CREATE, address));
    }

    @Operation(summary = "주소 단 건 조회", description = "주소 조회 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주소 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{addressId}")
    public ResponseEntity<CustomApiResponse<AddressResponseDto>> findAddressByAddressId(
        @PathVariable UUID addressId) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.ADDRESS_FIND_ONE,
                addressFacade.findByAddressId(addressId)));
    }

    @Operation(summary = "주소 검색", description = "주소 검색 메서드 입니다." +
        " 예시 http://localhost:8080/api/v1/addresses/search?addressName=busan&page=0&size=10&" +
        "addressName에 맞는 값이 없다면 전체를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주소 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<AddressSearchResponseDto>>> searchAddress(
        AddressSearchRequestDto requestDto, Pageable pageable) {

        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.ADDRESS_SEARCH,
                addressFacade.searchAddress(requestDto, pageable)));

    }

    @Operation(summary = "주소 수정", description = "주소 수정 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주소 수정 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PutMapping("/{addressId}")
    public ResponseEntity<CustomApiResponse<AddressResponseDto>> updateAddressByAddressId(
        @PathVariable UUID addressId,
        @Valid @RequestBody CreateAddressRequestDto requestDto
    ) {
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.ADDRESS_UPDATE,
            addressFacade.updateAddress(addressId, requestDto)));

    }

    @Operation(summary = "주소 삭제", description = "주소 삭제 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "지역 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{addressId}")
    public ResponseEntity<CustomApiResponse<URI>> deleteAddressByAddressId(
        @PathVariable UUID addressId,
        @CurrentLoginUserId Long userId) {
        addressFacade.deleteAddress(addressId, userId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/addresses")
            .build()
            .toUri();

        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.ADDRESS_DELETE, uri));
    }
}
