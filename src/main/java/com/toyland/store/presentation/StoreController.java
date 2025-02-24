/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.security.annotation.HasManageStoreRole;
import com.toyland.store.application.usecase.StoreService;
import com.toyland.store.application.usecase.dto.CreateStoreCategoryListServiceRequestDto;
import com.toyland.store.application.usecase.dto.DeleteStoreServiceRequestDto;
import com.toyland.store.application.usecase.dto.UpdateStoreServiceRequestDto;
import com.toyland.store.presentation.dto.CreateStoreCategoryListRequestDto;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.store.presentation.dto.SearchStoreRequestDto;
import com.toyland.store.presentation.dto.StoreResponseDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import com.toyland.store.presentation.dto.UpdateStoreRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "음식점", description = "Store API")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "상점 등록", description = "상점을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상점 등록 성공"),
    })
    @HasManageStoreRole
    @PostMapping
    public ResponseEntity<Void> createStore(@Valid @RequestBody CreateStoreRequestDto request) {
        StoreResponseDto store = storeService.createStore(request);
        return ResponseEntity.created(
            UriComponentsBuilder.fromUriString("/api/v1/stores/{storeId}")
                .buildAndExpand(store.id())
                .toUri()).build();
    }

    @Operation(summary = "상점의 카테고리 설정", description = "상점에 카테고리를 설정합니다. "
        + "기존 상점의 카테고리를 제거하고 새롭게 설정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상점의 카테고리 설정 성공"),
    })
    @HasManageStoreRole
    @PostMapping("/{storeId}/categories")
    public ResponseEntity<Void> setStoreCategories(
        @Valid @RequestBody CreateStoreCategoryListRequestDto request,
        @PathVariable UUID storeId,
        @CurrentLoginUserId Long loginUserId) {

        storeService.setStoreCategories(
            CreateStoreCategoryListServiceRequestDto
                .builder()
                .categoryIdList(request.categoryIdList())
                .storeId(storeId)
                .actorId(loginUserId)
                .eventTime(LocalDateTime.now())
                .build());
        return ResponseEntity.created(
            UriComponentsBuilder.fromUriString("/api/v1/stores/{storeId}")
                .buildAndExpand(storeId)
                .toUri()).build();
    }

    @Operation(summary = "상점 단 건 조회", description = "상점을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상점 조회 성공"),
    })
    @HasManageStoreRole
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> readStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeService.readStore(storeId));
    }

    @Operation(summary = "상점 검색", description = "상점을 검색합니다." +
        " (예시 파라미터: ?searchText=가&sort=name,desc,created_at&size=10&page=1)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상점 검색 성공"),
    })
    @GetMapping("/search")
    public ResponseEntity<Page<StoreWithOwnerResponseDto>> searchStores(
        @ModelAttribute SearchStoreRequestDto request) {
        return ResponseEntity.ok(storeService.searchStores(request));
    }

    @Operation(summary = "상점 수정", description = "상점을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상점 수정 성공"),
    })
    @HasManageStoreRole
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID storeId,
        @RequestBody UpdateStoreRequestDto request) {
        return ResponseEntity.ok(storeService.updateStore(
            UpdateStoreServiceRequestDto.of(request, storeId)));
    }

    @Operation(summary = "상점 삭제", description = "상점을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상점 삭제 성공"),
    })
    @HasManageStoreRole
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID storeId,
        @CurrentLoginUserId Long currentLoginUserId) {

        storeService.deleteStore(
            DeleteStoreServiceRequestDto.of(currentLoginUserId, storeId, LocalDateTime.now()));

        return ResponseEntity.noContent().build();
    }
}
