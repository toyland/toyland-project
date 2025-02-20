/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.category.application.usecase.dto.DeleteCategoryServiceRequestDto;
import com.toyland.category.application.usecase.dto.UpdateCategoryServiceRequestDto;
import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.time.LocalDateTime;
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

  @Autowired
  private UserRepository userRepository;

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

  @DisplayName("카테고리를 조회합니다.")
  @Test
  void readCategory(){
    // given
    Category parent = categoryRepository.save(createRootCategory("가게 배달"));
    Category category = categoryRepository.save(createCategory("가게 배달", parent));

    // when
    CategoryResponseDto categoryResponseDto = categoryService.readCategory(category.getId());

    // then
    assertThat(categoryResponseDto)
        .extracting("id","name", "parentCategoryId")
        .contains(category.getId(), category.getName(), parent.getId());
  }

  @DisplayName("카테고리를 수정합니다.")
  @Test
  void updateCategory(){
    // given
    Category category = categoryRepository.save(createRootCategory("치킨"));
    Category parent = categoryRepository.save(createRootCategory("가게 배달"));
    String newName = "피자";

    // when
    CategoryResponseDto categoryResponseDto = categoryService.updateCategory(
        UpdateCategoryServiceRequestDto.builder()
            .categoryId(category.getId())
            .name(newName)
            .parentCategoryId(parent.getId())
            .build()
    );

    // then
    assertThat(categoryResponseDto)
        .extracting("id","name", "parentCategoryId")
        .contains(category.getId(), newName, parent.getId());
  }

  @DisplayName("카테고리를 삭제합니다.")
  @Test
  void deleteCategory(){
    // given
    User user = userRepository.save(createMaster("유저1"));
    LocalDateTime now = LocalDateTime.now();
    Category category = categoryRepository.save(createRootCategory("치킨"));

    // when
    categoryService.deleteCategory(
        DeleteCategoryServiceRequestDto.of(user.getId(), category.getId(), now)
    );

    // then
    List<Category> all = categoryRepository.findAll();
    assertThat(all).hasSize(0);
  }

  private User createMaster(String username) {
    return new User(username, "password", UserRoleEnum.MASTER);
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