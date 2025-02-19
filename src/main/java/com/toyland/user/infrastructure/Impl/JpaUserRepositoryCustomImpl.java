package com.toyland.user.infrastructure.Impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.user.infrastructure.JpaUserRepositoryCustom;
import com.toyland.user.infrastructure.UserSortEnum;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.presentation.dto.UserSearchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.toyland.user.model.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class JpaUserRepositoryCustomImpl implements JpaUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> search(UserSearchRequestDto requestDto, Pageable pageable) {

        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        List<User> fetch = query(user, requestDto)
                .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        Long totalCount = query(Wildcard.count, requestDto).fetchOne();
        totalCount = Objects.nonNull(totalCount) ? totalCount : 0L;

        return new PageImpl<>(fetch, pageable, totalCount); // PageImpl 객체를 반환합니다.
    }

    private <T> JPAQuery<T> query(Expression<T> expr, UserSearchRequestDto requestDto) {
        return queryFactory
                .select(expr)
                .from(user)
                .where(
                        userContainsName(requestDto.getUsername()),
                        userRoleEq(requestDto.getRole())
                );
    }

    private List<OrderSpecifier<?>> dynamicOrder(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if(pageable.getSort().isSorted()) {
            for (Sort.Order sortOrder : pageable.getSort()) {

                Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

                UserSortEnum sortEnum = UserSortEnum.findByProperty(
                        sortOrder.getProperty());

                if (Objects.isNull(sortEnum)) {
                    throw new IllegalArgumentException(
                            "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
                }

                orderSpecifierList.add(
                        createOrderSpecifier(direction, sortEnum));
            }
        }else{
            orderSpecifierList.add(
                    createOrderSpecifier(Order.ASC, UserSortEnum.CREATED_AT));
        }

        return orderSpecifierList;
    }

    private BooleanExpression userContainsName(String username) {
        return Objects.nonNull(username) ? user.username.containsIgnoreCase(username) : null;
    }

    private BooleanExpression userRoleEq(UserRoleEnum role) {
        return Objects.nonNull(role) ? user.role.eq(role) : null;
    }

    private OrderSpecifier createOrderSpecifier(Order direction, UserSortEnum sortEnum) {
        boolean isAsc = direction == Order.ASC;
        return switch (sortEnum) {
            case CREATED_AT -> isAsc ? user.createdAt.asc() : user.createdAt.desc();
            case USER_NAME -> isAsc ? user.username.asc() : user.username.desc();
        };
    }
}
