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

    if (sort == null || sort.size() == 0) {
      orders.add(CategorySortType.CREATED_AT.getSpecifier(Direction.ASC));
      orders.add(CategorySortType.UPDATED_AT.getSpecifier(Direction.ASC));
      return orders.stream().toArray(OrderSpecifier[]::new);
    }

    Direction currentDirection = Direction.ASC;
    for (String param : sort) {
      String upperParam = param.replace("_","").toUpperCase();

      if (directionMap.containsKey(upperParam)) {
        currentDirection = directionMap.get(upperParam);
      } else if (sortTypeMap.containsKey(upperParam)) {
        orders.add(
            sortTypeMap.get(upperParam).getSpecifier(currentDirection));
        currentDirection = Direction.ASC;
      }
    }

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
