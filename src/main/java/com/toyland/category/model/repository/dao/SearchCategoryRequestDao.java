package com.toyland.category.model.repository.dao;

import static com.toyland.category.model.entity.QCategory.category;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

@Builder
public record SearchCategoryRequestDao (
    String searchText, UUID parentCategoryId, Integer page, Integer size, List<String> sort
){
  public long offset() {
    return (long) (this.page) * this.size;
  }

  public BooleanExpression getContainsName() {
    return StringUtils.hasText(searchText) ? category.name.containsIgnoreCase(searchText) : null;
  }

  public BooleanExpression getEqParentCategoryId() {
    return parentCategoryId != null ? category.parent.id.eq(parentCategoryId) : null;
  }

  public OrderSpecifier[] orderSpecifiers() {

    Map<String, CategorySortType> sortTypeMap = Map.of(
        "NAME", CategorySortType.NAME,
        "CREATEDAT", CategorySortType.CREATED_AT,
        "UPDATEDAT", CategorySortType.UPDATED_AT
    );

    Map<String, Direction> directionMap = Map.of(
        "ASC", Direction.ASC,
        "DESC", Direction.DESC
    );

    List<OrderSpecifier> orders = new ArrayList<>();

    boolean createdAtCheck = false;
    boolean updatedAtCheck = false;

    for (int i = 0; i < sort.size(); i++) {
      String param = sort.get(i);
      String upperParam = param.replace("_", "").toUpperCase();

      CategorySortType sortType = null;
      Direction direction = null;

      if (sortTypeMap.containsKey(upperParam)) {
        sortType = sortTypeMap.get(upperParam);
        if(sortType == CategorySortType.CREATED_AT) createdAtCheck = true;
        if(sortType == CategorySortType.UPDATED_AT) updatedAtCheck = true;
      }

      if (i + 1 < sort.size() && directionMap.containsKey(sort.get(i + 1).toUpperCase())) {
        i++;
        direction = directionMap.get(sort.get(i).toUpperCase());
      }

      if (sortType != null) {
        orders.add(
            sortTypeMap.get(upperParam)
                .getSpecifier(direction == null ? Direction.ASC : direction));
      }
    }

    if(!createdAtCheck) orders.add(CategorySortType.CREATED_AT.getSpecifier(Direction.ASC));
    if(!updatedAtCheck) orders.add(CategorySortType.UPDATED_AT.getSpecifier(Direction.ASC));

    return orders.stream().toArray(OrderSpecifier[]::new);
  }

  @RequiredArgsConstructor
  @Getter
  public enum CategorySortType {
    NAME {
      @Override
      public OrderSpecifier<?> getSpecifier(Direction direction) {
        return direction.isAscending() ? category.name.asc() : category.name.desc();
      }
    },
    CREATED_AT {
      @Override
      public OrderSpecifier<?> getSpecifier(Direction direction) {
        return direction.isAscending() ? category.createdAt.asc() : category.createdAt.desc();
      }
    },
    UPDATED_AT {
      @Override
      public OrderSpecifier<?> getSpecifier(Direction direction) {
        return direction.isAscending() ? category.createdAt.asc() : category.createdAt.desc();
      }
    };

    protected abstract OrderSpecifier<?> getSpecifier(Direction direction);
  }
}
