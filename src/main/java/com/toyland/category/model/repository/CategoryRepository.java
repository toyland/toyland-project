/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.model.repository;

import com.toyland.category.model.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

  Category save(Category entity);

  Optional<Category> findById(UUID id);

  List<Category> findAllById(Iterable<UUID> ids);

  // test 용
  List<Category> findAll();
}
