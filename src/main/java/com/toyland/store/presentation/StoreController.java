/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.store.application.facade.StoreFacade;
import com.toyland.store.presentation.dto.CreateStoreCategoryListRequestDto;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {
  private final StoreFacade storeFacade;

  /**
   * 음식점을 생성합니다.
   * @param request 음식점 생성 정보
   * @return 200 성공
   */
  @PostMapping
  public ResponseEntity<Void> createStore(@Valid @RequestBody CreateStoreRequestDto request) {
    storeFacade.createStore(request);
    return ResponseEntity.ok().build();
  }

  /**
   * 음식점에 카테고리들을 설정합니다.
   * @param request category id들
   * @param storeId 추가할 음식점
   * @param loginUserId 현재 로그인 유저
   * @return 200 성공
   */
  @PostMapping("/{storeId}/categories")
  public ResponseEntity<Void> setStoreCategories(
      @Valid @RequestBody CreateStoreCategoryListRequestDto request,
      @PathVariable UUID storeId,
      @CurrentLoginUserId Long loginUserId) {

    storeFacade.setStoreCategories(request, storeId, loginUserId, LocalDateTime.now());

    return ResponseEntity.created(
        UriComponentsBuilder.fromUriString("/api/v1/{storeId}")
            .buildAndExpand(storeId)
            .toUri()).build();
  }
}
