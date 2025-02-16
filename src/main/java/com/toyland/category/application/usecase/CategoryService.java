/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import com.toyland.category.presentation.dto.CreateCategoryRequestDto;

public interface CategoryService {

  void createCategory(CreateCategoryRequestDto request);
}
