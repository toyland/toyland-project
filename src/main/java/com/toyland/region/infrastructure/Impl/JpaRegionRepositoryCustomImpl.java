package com.toyland.region.infrastructure.Impl;

import static com.toyland.region.model.entity.QRegion.region;
import static com.toyland.store.model.entity.QStore.store;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.region.infrastructure.JpaRegionRepositoryCustom;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
@RequiredArgsConstructor
public class JpaRegionRepositoryCustomImpl implements JpaRegionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        List<RegionSearchResponseDto> searchRegionResponseList = queryFactory.select(
                Projections.fields(RegionSearchResponseDto.class,
                    region.id,
                    region.regionName,
                    store.id,
                    store.name))
            .from(region)
            .join(region.storeList, store).fetchJoin()
            .where(
                store.region.id.eq(searchRequestDto.regionId()),
                regionNameContains(searchRequestDto.regionName()),
                storeNameContains(searchRequestDto.storeName())
            )
            .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = queryFactory.select(region.countDistinct())
            .from(region)
            .where(
                store.region.id.eq(searchRequestDto.regionId()),
                regionNameContains(searchRequestDto.regionName()),
                storeNameContains(searchRequestDto.storeName())
            ).fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(searchRegionResponseList, pageable, totalCount);

    }

    private List<OrderSpecifier<?>> dynamicOrder(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orderSpecifierList.add(new OrderSpecifier<>(direction, region.createdAt));
                        break;
                    case "regionName":
                        orderSpecifierList.add(new OrderSpecifier<>(direction, region.regionName));
                    default:
                        throw new IllegalArgumentException(
                            "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
                }

            }
        } else {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, region.createdAt));
        }
        return orderSpecifierList;
    }

    private BooleanExpression storeNameContains(String storeName) {
        return storeName != null ? store.name.containsIgnoreCase(storeName) : null;
    }

    private BooleanExpression regionNameContains(String regionName) {
        return regionName != null ? region.regionName.containsIgnoreCase(regionName) : null;
    }
}
