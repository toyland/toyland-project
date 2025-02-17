/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.CategoryErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public void createCategory(CreateCategoryRequestDto dto) {
    Category parent = dto.patentId() == null ? null : findCategoryById(dto.patentId());
    categoryRepository.save(Category.from(dto, parent));
  }

  private Category findCategoryById(UUID id) {
    return categoryRepository.findById(id).orElseThrow(
        () -> CustomException.from(CategoryErrorCode.CATEGORY_NOT_FOUND)
    );
  }

}
