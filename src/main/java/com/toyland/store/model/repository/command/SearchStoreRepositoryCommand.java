/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.repository.command;

import static com.toyland.category.model.entity.QCategory.category;
import static com.toyland.store.model.entity.QStore.store;

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
public record SearchStoreRepositoryCommand(
    String searchText, String categoryNameSearchText, String storeNameSearchText,
    String storeSearchAddress,
    UUID regionId, Long ownerId, UUID categoryId, Integer page, Integer size, List<String> sort
) {

    public long offset() {
        return (long) (this.page) * this.size;
    }

    public BooleanExpression getContainsSearchText() {
        return StringUtils.hasText(searchText)
            ? store.name.containsIgnoreCase(searchText)
            .or(store.content.containsIgnoreCase(searchText))
            .or(category.name.containsIgnoreCase(searchText))
            : null;
    }

    public BooleanExpression getContainsCategorySearchText() {
        return StringUtils.hasText(categoryNameSearchText)
            ? category.name.containsIgnoreCase(categoryNameSearchText) : null;
    }

    public BooleanExpression getContainsStoreNameSearchText() {
        return StringUtils.hasText(storeNameSearchText)
            ? store.name.containsIgnoreCase(storeNameSearchText) : null;
    }

    public BooleanExpression getContainsStoreNameSearchAddress() {
        return StringUtils.hasText(storeSearchAddress)
            ? store.address.containsIgnoreCase(storeSearchAddress) : null;
    }


    public BooleanExpression getEqRegionId() {
        return regionId != null ? store.region.id.eq(regionId) : null;
    }

    public BooleanExpression getEqOwnerId() {
        return ownerId != null ? store.owner.id.eq(ownerId) : null;
    }

    public BooleanExpression getEqCategoryId() {
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    public OrderSpecifier[] orderSpecifiers() {

        Map<String, StoreSortType> sortTypeMap = Map.of(
            "NAME", StoreSortType.NAME,
            "CREATEDAT", StoreSortType.CREATED_AT,
            "UPDATEDAT", StoreSortType.UPDATED_AT
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

            StoreSortType sortType = null;
            Direction direction = null;

            if (sortTypeMap.containsKey(upperParam)) {
                sortType = sortTypeMap.get(upperParam);
                if (sortType == StoreSortType.CREATED_AT) {
                    createdAtCheck = true;
                }
                if (sortType == StoreSortType.UPDATED_AT) {
                    updatedAtCheck = true;
                }
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

        if (!createdAtCheck) {
            orders.add(StoreSortType.CREATED_AT.getSpecifier(Direction.ASC));
        }
        if (!updatedAtCheck) {
            orders.add(StoreSortType.UPDATED_AT.getSpecifier(Direction.ASC));
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    @RequiredArgsConstructor
    @Getter
    public enum StoreSortType {
        NAME {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? store.name.asc() : store.name.desc();
            }
        },
        CREATED_AT {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? store.createdAt.asc() : store.createdAt.desc();
            }
        },
        UPDATED_AT {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? store.createdAt.asc() : store.createdAt.desc();
            }
        };

        protected abstract OrderSpecifier<?> getSpecifier(Direction direction);
    }
}
