/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import com.toyland.category.application.usecase.dto.DeleteCategoryServiceRequestDto;
import com.toyland.category.application.usecase.dto.UpdateCategoryServiceRequestDto;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import java.util.UUID;

public interface CategoryService {

  void createCategory(CreateCategoryRequestDto request);

  CategoryResponseDto readCategory(UUID categoryId);

  CategoryResponseDto updateCategory(UpdateCategoryServiceRequestDto dto);

  void deleteCategory(DeleteCategoryServiceRequestDto dto);
}
