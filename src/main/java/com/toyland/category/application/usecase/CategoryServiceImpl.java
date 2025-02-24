/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.application.usecase;

import com.toyland.category.application.usecase.dto.DeleteCategoryServiceRequestDto;
import com.toyland.category.application.usecase.dto.UpdateCategoryServiceRequestDto;
import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.category.model.repository.dao.SearchCategoryRequestDao;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import com.toyland.category.presentation.dto.SearchCategoryRequestDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.CategoryErrorCode;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public CategoryResponseDto createCategory(CreateCategoryRequestDto dto) {
    Category parent = dto.patentId() == null ? null : findCategoryById(dto.patentId());
    return CategoryResponseDto.from(categoryRepository.save(Category.from(dto, parent)));
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponseDto readCategory(UUID categoryId) {
    return CategoryResponseDto.from(findCategoryById(categoryId));
  }

  @Override
  public Page<CategoryResponseDto> searchCategories(SearchCategoryRequestDto dto) {
    return categoryRepository.searchCategories(
        SearchCategoryRequestDao.builder()
            .searchText(dto.searchText())
            .parentCategoryId(dto.parentCategoryId())
            .page(dto.page() - 1)
            .size(Set.of(10, 30, 50).contains(dto.size()) ? dto.size() : 10)
            .build());
  }

  @Override
  @Transactional
  public CategoryResponseDto updateCategory(UpdateCategoryServiceRequestDto dto) {
    if(dto.isEqualsParentCategoryId()){
      throw CustomException.from(CategoryErrorCode.INVALID_PARENT_ID);
    }

    Map<UUID, Category> categories = categoryRepository.findAllById(
        dto.getCategoryList())
        .stream().collect(Collectors.toMap(Category::getId, entity -> entity));

    Category category = categories.get(dto.categoryId());
    Category parentCategory = categories.get(dto.parentCategoryId());
    category.update(dto, parentCategory);

    return CategoryResponseDto.from(category);
  }

  @Override
  @Transactional
  public void deleteCategory(DeleteCategoryServiceRequestDto dto) {
    Category category = findCategoryById(dto.categoryId());
    category.delete(dto.eventTime(), dto.actorId());
  }

  private Category findCategoryById(UUID id) {
    return categoryRepository.findById(id).orElseThrow(
        () -> CustomException.from(CategoryErrorCode.CATEGORY_NOT_FOUND)
    );
  }

}
