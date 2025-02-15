/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public void createCategory(CreateCategoryRequestDto dto) {

  }

}
