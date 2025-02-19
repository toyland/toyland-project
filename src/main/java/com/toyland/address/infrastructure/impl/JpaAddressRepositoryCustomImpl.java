package com.toyland.address.infrastructure.impl;


import static com.toyland.address.model.entity.QAddress.address;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.address.infrastructure.JpaAddressRepositoryCustom;
import com.toyland.address.model.entity.Address;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 19.
 */
@RequiredArgsConstructor
public class JpaAddressRepositoryCustomImpl implements JpaAddressRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<AddressSearchResponseDto> searchAddress(AddressSearchRequestDto requestDto,
        Pageable pageable) {

        int pageSize = validatePageSize(pageable.getPageSize());

        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        List<Address> fetch = query(address, requestDto)
            .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageSize)
            .fetch();

        List<AddressSearchResponseDto> addressSearchResponseDtoList = fetch.stream()
            .map(AddressSearchResponseDto::from)
            .collect(Collectors.toList());

        Long totalCount = query(Wildcard.count, requestDto).fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(addressSearchResponseDtoList, pageable, totalCount);


    }

    private int validatePageSize(int pageSize) {
        return Set.of(10, 30, 50).contains(pageSize) ? pageSize : 10;
    }

    private <T> JPAQuery<T> query(Expression<T> expr, AddressSearchRequestDto requestDto) {
        return queryFactory
            .select(expr)
            .from(address)
            .where(
                addressNameContains(requestDto.addressName())
            );
    }


    private BooleanExpression addressNameContains(String addressName) {
        return addressName != null ? address.addressName.containsIgnoreCase(addressName) : null;
    }


    private List<OrderSpecifier<?>> dynamicOrder(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orderSpecifierList.add(new OrderSpecifier<>(direction, address.createdAt));
                        break;
                    case "updatedAt":
                        orderSpecifierList.add(new OrderSpecifier<>(direction, address.updatedAt));
                        break;
                    case "addressName":
                        orderSpecifierList.add(
                            new OrderSpecifier<>(direction, address.addressName));
                        break;
                    default:
                        throw new IllegalArgumentException(
                            "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
                }
            }
        } else {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, address.createdAt));
        }
        return orderSpecifierList;

    }

}
