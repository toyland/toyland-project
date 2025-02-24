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
public class StoreController {
  private final StoreService storeService;

  /**
   * 음식점을 생성합니다.
   * @param request 음식점 생성 정보
   * @return 200 성공
   */
  @HasManageStoreRole
  @PostMapping
  public ResponseEntity<Void> createStore(@Valid @RequestBody CreateStoreRequestDto request) {
    StoreResponseDto store = storeService.createStore(request);
    return ResponseEntity.created(
        UriComponentsBuilder.fromUriString("/api/v1/stores/{storeId}")
            .buildAndExpand(store.id())
            .toUri()).build();
  }

  /**
   * 음식점에 카테고리들을 설정합니다.
   * @param request category id들
   * @param storeId 추가할 음식점
   * @param loginUserId 현재 로그인 유저
   * @return 200 성공
   */
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

  /**
   * 음식점을 조회합니다.
   * @param storeId 조회할 음식점 id
   * @return 조회 음식점 dto
   */
  @HasManageStoreRole
  @GetMapping("/{storeId}")
  public ResponseEntity<StoreResponseDto> readStore(@PathVariable UUID storeId) {
    return ResponseEntity.ok(storeService.readStore(storeId));
  }

  @GetMapping("/search")
  public ResponseEntity<Page<StoreWithOwnerResponseDto>> searchStores(
      @ModelAttribute SearchStoreRequestDto request){
    return ResponseEntity.ok(storeService.searchStores(request));
  }

  /**
   * 음식점을 수정합니다.
   * @param storeId 수정할 store id
   * @param request 수정 내용
   * @return 수정된 내용
   */
  @HasManageStoreRole
  @PutMapping("/{storeId}")
  public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID storeId,
      @RequestBody UpdateStoreRequestDto request) {
    return ResponseEntity.ok(storeService.updateStore(
        UpdateStoreServiceRequestDto.of(request, storeId)));
  }

  /**
   * 음식점을 삭제합니다.
   * @param storeId 삭제할 음식점 id
   * @return 204 no content
   */
  @HasManageStoreRole
  @DeleteMapping("/{storeId}")
  public ResponseEntity<Void> deleteStore(@PathVariable UUID storeId,
      @CurrentLoginUserId Long currentLoginUserId) {

    storeService.deleteStore(
        DeleteStoreServiceRequestDto.of(currentLoginUserId, storeId, LocalDateTime.now()));

    return ResponseEntity.noContent().build();
  }
}
