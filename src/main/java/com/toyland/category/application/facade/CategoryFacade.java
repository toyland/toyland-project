/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.facade;

import com.toyland.category.presentation.dto.CreateCategoryRequestDto;

public interface CategoryFacade {

  void createCategory(CreateCategoryRequestDto request);
}
