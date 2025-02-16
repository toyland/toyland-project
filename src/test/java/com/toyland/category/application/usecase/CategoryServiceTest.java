/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.common.IntegrationTestSupport;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryServiceTest extends IntegrationTestSupport {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private CategoryRepository categoryRepository;

  @AfterEach
  void tearDown() {
    categoryRepository.deleteAllInBatch();
  }

  @DisplayName("가게 배달 루트 카테고리를 저장한다.")
  @Test
  void createCategory() {
    // when
    categoryService.createCategory(new CreateCategoryRequestDto(null, "가게 배달"));

    // then
    List<Category> all = categoryRepository.findAll();
    assertThat(all).hasSize(1)
        .extracting("name", "parent")
        .containsExactlyInAnyOrder(
            tuple("가게 배달", null)
        );
  }

  @DisplayName("가게 배달의 치킨 카테고리를 저장한다.")
  @Test
  void createCategoryWithParentCategory() {
    // given
    Category parent = createRootCategory("가게 배달");
    Category savedParent = categoryRepository.save(parent);

    // when
    categoryService.createCategory(new CreateCategoryRequestDto(savedParent.getId(), "치킨"));

    // then
    List<Category> all = categoryRepository.findAll();
    assertThat(all).hasSize(2)
        .extracting("name", "parent.name")
        .containsExactlyInAnyOrder(
            tuple("가게 배달", null),
            tuple("치킨", "가게 배달")
        );
  }

  private Category createRootCategory(String name){
    return this.createCategory(name, null);
  }

  private Category createCategory(String name, Category parent){
    return Category.builder()
        .name(name)
        .parent(parent)
        .build();
  }
}