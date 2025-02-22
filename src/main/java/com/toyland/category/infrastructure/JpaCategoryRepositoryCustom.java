/**
 * @Date : 2025. 02. 21.
 * @author : jieun(je-pa)
 */
package com.toyland.category.infrastructure;

import com.toyland.category.model.repository.dao.SearchCategoryRequestDao;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import org.springframework.data.domain.Page;

public interface JpaCategoryRepositoryCustom {

  Page<CategoryResponseDto> searchCategories(SearchCategoryRequestDao dto);

}
