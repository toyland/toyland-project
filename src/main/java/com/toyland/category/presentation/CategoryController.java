/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.presentation;

import com.toyland.category.application.usecase.CategoryService;
import com.toyland.category.application.usecase.dto.DeleteCategoryServiceRequestDto;
import com.toyland.category.application.usecase.dto.UpdateCategoryServiceRequestDto;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.category.presentation.dto.SearchCategoryRequestDto;
import com.toyland.category.presentation.dto.UpdateCategoryRequestDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.security.annotation.HasManageCategoryRole;
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

@HasManageCategoryRole
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "카테고리", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 등록", description = "카테고리를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 등록 성공"),
    })
    @PostMapping
    public ResponseEntity<Void> createCategory(
        @Valid @RequestBody CreateCategoryRequestDto request) {
        CategoryResponseDto category = categoryService.createCategory(request);
        return ResponseEntity.created(
            UriComponentsBuilder.fromUriString("/api/v1/categories/{categoryId}")
                .buildAndExpand(category.id())
                .toUri()).build();
    }

    @Operation(summary = "카테고리 단 건 조회", description = "카테고리를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> readCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(categoryService.readCategory(categoryId));
    }

    @Operation(summary = "카테고리 검색", description = "카테고리를 검색합니다." +
        " (예시 파라미터: ?searchText=가&sort=name,desc,created_at&size=10&page=1)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 검색 성공"),
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponseDto>> searchCategories(
        @ModelAttribute SearchCategoryRequestDto request
    ) {
        return ResponseEntity.ok(categoryService.searchCategories(request));
    }

    @Operation(summary = "카테고리 수정", description = "카테로리를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId,
        @RequestBody UpdateCategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.updateCategory(
            UpdateCategoryServiceRequestDto.of(request, categoryId)));
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "지역 삭제 성공"),
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId,
        @CurrentLoginUserId Long currentLoginUserId) {

        categoryService.deleteCategory(
            DeleteCategoryServiceRequestDto.of(currentLoginUserId, categoryId,
                LocalDateTime.now()));

        return ResponseEntity.noContent().build();
    }
}
