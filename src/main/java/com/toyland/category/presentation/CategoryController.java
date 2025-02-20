/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.presentation;

import com.toyland.category.application.facade.CategoryFacade;
import com.toyland.category.application.usecase.CategoryService;
import com.toyland.category.application.usecase.dto.DeleteCategoryServiceRequestDto;
import com.toyland.category.application.usecase.dto.UpdateCategoryServiceRequestDto;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  private final CategoryFacade categoryFacade;
  private final CategoryService categoryService;

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

  /**
   * 카테고리을 조회합니다.
   * @param categoryId 조회할 카테고리 id
   * @return 조회 카테고리 dto
   */
  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDto> readCategory(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(categoryService.readCategory(categoryId));
  }

  /**
   * 카테고리을 수정합니다.
   * @param categoryId 수정할 category id
   * @param request 수정 내용
   * @return 수정된 내용
   */
  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId,
      @RequestBody UpdateCategoryRequestDto request) {
    return ResponseEntity.ok(categoryService.updateCategory(
        UpdateCategoryServiceRequestDto.of(request, categoryId)));
  }

  /**
   * 카테고리을 삭제합니다.
   * @param categoryId 삭제할 카테고리 id
   * @return 204 no content
   */
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId,
      @CurrentLoginUserId Long currentLoginUserId) {

    categoryService.deleteCategory(
        DeleteCategoryServiceRequestDto.of(currentLoginUserId, categoryId, LocalDateTime.now()));

    return ResponseEntity.noContent().build();
  }
}
