package com.toyland.region.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.region.application.facade.RegionFacade;
import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
@Tag(name = "지역", description = "Region API")
public class RegionController {

    private final RegionFacade regionFacade;


    @Operation(summary = "지역 등록", description = "지역 등록 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지역 등록 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RegionResponseDto>> createRegion(
        @Valid @RequestBody CreateRegionRequestDto requestDto) {
        RegionResponseDto region = regionFacade.createRegion(requestDto);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/{regionId}")
            .buildAndExpand(region.regionId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(CustomApiResponse.of(HttpSuccessCode.REGION_CREATE, region));

    }

    @Operation(summary = "지역 단 건 조회", description = "지역 단 건 조회 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지역 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{regionId}")
    public ResponseEntity<CustomApiResponse<RegionResponseDto>> findRegionByRegionId(
        @PathVariable UUID regionId) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.REGION_FIND_ONE,
                regionFacade.findByRegionId(regionId)));
    }

    @Operation(summary = "지역 검색", description = "지역 검색 메서드 입니다." +
        " 예시 http://localhost:8080/api/v1/regions/search?regionName=서울시&page=1&size=5&sort=createdAt,asc"
        +
        "regionName 맞는 값이 없다면 전체를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지역 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<RegionSearchResponseDto>>> searchRegion(
        @ModelAttribute RegionSearchRequestDto searchRequestDto) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.REGION_SEARCH,
                regionFacade.searchRegion(searchRequestDto)));
    }


    @Operation(summary = "지역 수정", description = "지역 수정 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지역 수정 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_MASTER')")
    @PutMapping("/{regionId}")
    public ResponseEntity<CustomApiResponse<RegionResponseDto>> updateRegionByRegionId(
        @PathVariable UUID regionId,
        @Valid @RequestBody CreateRegionRequestDto requestDto) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.REGION_UPDATE,
                regionFacade.updateRegion(regionId, requestDto)));
    }

    @Operation(summary = "지역 삭제", description = "지역 삭제 메서드 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "지역 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_MASTER')")
    @DeleteMapping("/{regionId}")
    public ResponseEntity<CustomApiResponse<URI>> deleteRegionByRegionId(
        @PathVariable UUID regionId,
        @CurrentLoginUserId Long userId) {
        regionFacade.deleteByRegionId(regionId, userId);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/regions")
            .build()
            .toUri();
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.REGION_DELETE, uri));
    }
}
