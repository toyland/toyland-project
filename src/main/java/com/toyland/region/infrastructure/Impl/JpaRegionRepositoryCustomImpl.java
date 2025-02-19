package com.toyland.region.infrastructure.Impl;


import static com.toyland.region.model.entity.QRegion.region;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.region.infrastructure.JpaRegionRepositoryCustom;
import com.toyland.region.model.entity.Region;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
@RequiredArgsConstructor
@Slf4j
public class JpaRegionRepositoryCustomImpl implements JpaRegionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        int pageSize = validatePageSize(pageable.getPageSize());

        List<Region> fetch = query(region, searchRequestDto)
            .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageSize)
            .distinct()
            .fetch();

        // RegionSearchResponseDto로 변환
        List<RegionSearchResponseDto> regionSearchResponseDtos = fetch.stream()
            .map(RegionSearchResponseDto::from)
            .collect(Collectors.toList());

        Long totalCount = query(Wildcard.count, searchRequestDto).fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(regionSearchResponseDtos, pageable, totalCount);

    }

    private <T> JPAQuery<T> query(Expression<T> expr, RegionSearchRequestDto searchRequestDto) {
        return queryFactory
            .select(expr)
            .from(region)
            .where(
                regionNameContains(searchRequestDto.regionName())
            );
    }

    private int validatePageSize(int pageSize) {
        return Set.of(10, 30, 50).contains(pageSize) ? pageSize : 10;
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

    private BooleanExpression regionNameContains(String regionName) {
        return regionName != null ? region.regionName.containsIgnoreCase(regionName) : null;
    }
}
