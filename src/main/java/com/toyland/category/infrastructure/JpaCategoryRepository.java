/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.infrastructure;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, UUID>,
    JpaCategoryRepositoryCustom {

}
