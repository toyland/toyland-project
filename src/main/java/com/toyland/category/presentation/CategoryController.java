/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.presentation;

import com.toyland.category.application.facade.CategoryFacade;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  private final CategoryFacade categoryFacade;

  /**
   * 카테고리를 생성합니다.
   * @param request 카테고리 생성 정보
   * @return 200 성공
   */
  @PostMapping
  public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryRequestDto request) {
    categoryFacade.createCategory(request);
    return ResponseEntity.ok().build();
  }
}
