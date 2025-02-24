package com.toyland.category.infrastructure.impl;

import static com.toyland.category.model.entity.QCategory.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.category.infrastructure.JpaCategoryRepositoryCustom;
import com.toyland.category.model.entity.Category;
import com.toyland.category.model.entity.QCategory;
import com.toyland.category.model.repository.dao.SearchCategoryRequestDao;
import com.toyland.category.presentation.dto.CategoryResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class JpaCategoryRepositoryCustomImpl implements
    JpaCategoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<CategoryResponseDto> searchCategories(SearchCategoryRequestDao dto) {
    QCategory parentCategory = new QCategory("parentCategory"); // 별칭 추가

    List<Category> categoryList = queryFactory
        .select(category)
        .from(category)
        .leftJoin(category.parent, parentCategory)
        .where(
            dto.getContainsName(),
            dto.getEqParentCategoryId()
        )
        .orderBy(dto.orderSpecifiers())
        .offset(dto.offset())
        .limit(dto.size())
        .fetch();

    long totalCount = queryFactory
        .select(category.count())
        .from(category)
        .leftJoin(category.parent, parentCategory)
        .where(
            dto.getContainsName(),
            dto.getEqParentCategoryId()
        )
        .fetchOne();

    List<CategoryResponseDto> collect = categoryList.stream().map(CategoryResponseDto::from)
        .collect(Collectors.toList());

    return new PageImpl<>(collect, PageRequest.of(dto.page(), dto.size()), totalCount);
  }
}
