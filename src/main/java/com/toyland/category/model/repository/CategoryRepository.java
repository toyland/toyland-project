/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.model.repository;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.dao.SearchCategoryRequestDao;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CategoryRepository {

  Category save(Category entity);

  Optional<Category> findById(UUID id);

  Page<CategoryResponseDto> searchCategories(SearchCategoryRequestDao dao);

  List<Category> findAllById(Iterable<UUID> ids);

  // test 용
  List<Category> findAll();

  <S extends Category> Iterable<S> saveAll(Iterable<S> entities);
}
