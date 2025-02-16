/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation;

import com.toyland.store.application.facade.StoreFacade;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
