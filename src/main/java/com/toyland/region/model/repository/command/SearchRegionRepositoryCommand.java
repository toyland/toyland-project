package com.toyland.region.model.repository.command;

import static com.toyland.region.model.entity.QRegion.region;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 24.
 */
@Builder
public record SearchRegionRepositoryCommand(String regionName,
                                            Integer page,
                                            Integer size,
                                            List<String> sort) {

    public long offset() {
        return (long) (this.page) * this.size;
    }

    public BooleanExpression regionNameContains() {
        return StringUtils.hasText(regionName) ?
            region.regionName.containsIgnoreCase(regionName) : null;
    }

    public OrderSpecifier[] orderSpecifiers() {

        Map<String, RegionSortType> sortTypeMap = Map.of(
            "NAME", RegionSortType.NAME,
            "CREATEDAT", RegionSortType.CREATED_AT,
            "UPDATEDAT", RegionSortType.UPDATE_AT
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

            RegionSortType sortType = null;
            Direction direction = null;

            if (sortTypeMap.containsKey(upperParam)) {
                sortType = sortTypeMap.get(upperParam);
                if (sortType == RegionSortType.CREATED_AT) {
                    createdAtCheck = true;
                }
                if (sortType == RegionSortType.UPDATE_AT) {
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
            orders.add(RegionSortType.CREATED_AT.getSpecifier(Direction.ASC));
        }
        if (!updatedAtCheck) {
            orders.add(RegionSortType.UPDATE_AT.getSpecifier(Direction.ASC));
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    @RequiredArgsConstructor
    @Getter
    public enum RegionSortType {
        NAME {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? region.regionName.asc() : region.regionName.desc();
            }
        },
        CREATED_AT {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? region.createdAt.asc() : region.createdAt.desc();
            }
        },
        UPDATE_AT {
            @Override
            public OrderSpecifier<?> getSpecifier(Direction direction) {
                return direction.isAscending() ? region.updatedAt.asc() : region.updatedAt.desc();
            }
        };

        protected abstract OrderSpecifier<?> getSpecifier(Direction direction);
    }

}
