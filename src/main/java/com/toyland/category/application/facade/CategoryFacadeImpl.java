/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.facade;

import com.toyland.category.application.usecase.CategoryService;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryFacadeImpl implements CategoryFacade{
  private final CategoryService categoryService;

  @Override
  public void createCategory(CreateCategoryRequestDto request) {
    categoryService.createCategory(request);
  }
}
