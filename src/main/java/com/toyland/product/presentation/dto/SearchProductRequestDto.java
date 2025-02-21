/**
 * @Date : 2025. 02. 21.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.dto;

import static com.toyland.product.model.entity.QProduct.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.toyland.product.presentation.dto.SearchProductRequestDto.Order.ProductSortType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

@Setter
@Builder
public class SearchProductRequestDto {

  private String searchText;
  private UUID storeId;
  private Boolean isDisplay;
  private int page = 1;
  private int size = 10;
  private List<String> sort;

  public int getValidateSize() {
    return size < 10 ? 10 : size - size % 10;
  }

  public int getPage(){
    return page - 1;
  }

  public long getOffset() {
    return (long) (this.page - 1) * this.size;
  }

  public OrderSpecifier[] getOrderSpecifiers() {
    return this.getOrderList().stream()
        .map(order -> order.getSpecifier()).toArray(OrderSpecifier[]::new);
  }

  // 상품명 검색 조건
  public BooleanExpression getContainsName() {
    return StringUtils.hasText(searchText) ? product.name.containsIgnoreCase(searchText) : null;
  }

  // 특정 스토어 ID 필터링
  public BooleanExpression getEqStoreId() {
    return storeId != null ? product.store.id.eq(storeId) : null;
  }

  // isDisplay 필터링
  public BooleanExpression getEqIsDisplay() {
    return isDisplay != null ? product.isDisplay.eq(isDisplay) : null;
  }

  private List<Order> getOrderList() {

    Map<String, ProductSortType> sortTypeMap = Map.of(
        "NAME", ProductSortType.NAME,
        "CREATEDAT", ProductSortType.CREATED_AT,
        "UPDATEDAT", ProductSortType.UPDATED_AT
    );

    Map<String, Direction> directionMap = Map.of(
        "ASC", Direction.ASC,
        "DESC", Direction.DESC
    );

    List<Order> orders = new ArrayList<>();
    if (sort == null || sort.size() == 0) {
      orders.add(new Order(ProductSortType.CREATED_AT, Direction.ASC));
      orders.add(new Order(ProductSortType.UPDATED_AT, Direction.ASC));
      return orders;
    }
    Direction currentDirection = Direction.ASC;
    for (String param : sort) {
      String upperParam = param.replace("_","").toUpperCase();

      if (directionMap.containsKey(upperParam)) {
        currentDirection = directionMap.get(upperParam);
      } else if (sortTypeMap.containsKey(upperParam)) {
        orders.add(
            new Order(sortTypeMap.get(upperParam), currentDirection));
        currentDirection = Direction.ASC;
      }
    }

    return orders;
  }

  public record Order(
      ProductSortType sortType,
      Direction direction
  ) {

    public OrderSpecifier<?> getSpecifier() {
      return this.sortType.getSpecifier(this.direction);
    }

    @RequiredArgsConstructor
    @Getter
    public enum ProductSortType {
      NAME {
        @Override
        public OrderSpecifier<?> getSpecifier(Direction direction) {
          return direction.isAscending() ? product.name.asc() : product.name.desc();
        }
      },
      CREATED_AT {
        @Override
        public OrderSpecifier<?> getSpecifier(Direction direction) {
          return direction.isAscending() ? product.createdAt.asc() : product.createdAt.desc();
        }
      },
      UPDATED_AT {
        @Override
        public OrderSpecifier<?> getSpecifier(Direction direction) {
          return direction.isAscending() ? product.createdAt.asc() : product.createdAt.desc();
        }
      };

      protected abstract OrderSpecifier<?> getSpecifier(Direction direction);
    }
  }
}
