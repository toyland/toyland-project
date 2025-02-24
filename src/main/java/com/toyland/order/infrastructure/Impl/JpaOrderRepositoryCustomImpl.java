package com.toyland.order.infrastructure.Impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.order.infrastructure.JpaOrderRepositoryCustom;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import com.toyland.user.model.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.toyland.order.model.QOrder.order;
import static com.toyland.orderproduct.model.QOrderProduct.orderProduct;
import static com.toyland.product.model.entity.QProduct.product;
import static com.toyland.store.model.entity.QStore.store;
import static com.toyland.user.model.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class JpaOrderRepositoryCustomImpl implements JpaOrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 주문 동적 검색 메서드
     * @param searchRequestDto : 검색 조건 (orderId, userId)
     * @param pageable : 페이징 및 정렬 정보
     * @return Page<OrderSearchResponseDto>
     */
    @Override
    public Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto, Pageable pageable, Long loginUserId, UserRoleEnum role) {
        // 동적 정렬 조건 생성
        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        // 유효한 페이지 크기 설정
        int pageSize = validatePageSize(pageable.getPageSize());


        // 동적 쿼리 생성 및 조회
        List<Order> fetch = queryFactory
                .select(order)
                .from(order)
                .leftJoin(order.user, user).fetchJoin()                     // User 조인
                .leftJoin(order.orderProductList, orderProduct).fetchJoin() // OrderProduct 조인
                .leftJoin(orderProduct.product, product).fetchJoin()        // Product 조인
                .leftJoin(product.store, store).fetchJoin()                 // Store 조인
                .where(
                        loginUserIdEq(loginUserId, role),                   // 실제 로그인 유저 아이디
                        userIdEq(searchRequestDto.userId(), role),          // 검색을 하기위한 유저 아이디
                        orderIdEq(searchRequestDto.orderId()),
                        storeIdEq(searchRequestDto.storeId(), role),
                        orderStatusIdEq(searchRequestDto.orderStatus())
                )
                .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0])) // 동적 정렬
                .offset(pageable.getOffset())  // 페이징 - 시작 인덱스
                .limit(pageSize)               // 페이징 - 페이지 크기
                .distinct()                    // 중복 제거
                .fetch();                      // 조회 실행



        // Order -> OrderSearchResponseDto 변환
        List<OrderSearchResponseDto> orderSearchResponseDtos = fetch.stream()
                .map(OrderSearchResponseDto::from)
                .collect(Collectors.toList());

        // 총 개수 조회 (COUNT 쿼리)
        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(order)
                .leftJoin(order.user, user)                     // User 조인
                .leftJoin(order.orderProductList, orderProduct) // OrderProduct 조인
                .leftJoin(orderProduct.product, product)        // Product 조인
                .leftJoin(product.store, store)                 // Store 조인
                .where(
                        loginUserIdEq(loginUserId, role),
                        orderIdEq(searchRequestDto.orderId()),
                        userIdEq(searchRequestDto.userId(), role),
                        storeIdEq(searchRequestDto.storeId(), role),
                        orderStatusIdEq(searchRequestDto.orderStatus())
                )
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        // Page 객체 생성 후 반환
        return new PageImpl<>(orderSearchResponseDtos, pageable, totalCount);
    }



    // 실제 로그인 유저 아이디
    private BooleanExpression loginUserIdEq(Long loginUserId, UserRoleEnum role) {
        if (role == UserRoleEnum.OWNER || role == UserRoleEnum.MANAGER || role == UserRoleEnum.MASTER) {
            loginUserId = null; // (OWNER, MANAGER, MASTER)은 모든 주문을 조회할 수 있지만, 자신이 주문한 목록은 볼 수 없다는 전제, 주문자가 아닌 '관리자' 역할만 수행
        }
        return loginUserId != null ? order.user.id.eq(loginUserId) : null;
    }


    // 검색할 로그인 유저 아이디
    private BooleanExpression userIdEq(Long userId, UserRoleEnum role) {
        // CUSTOMER는 특정 userId로 검색 불가
        if (role == UserRoleEnum.CUSTOMER) {
            return null;
        }
        return userId != null ? order.user.id.eq(userId) : null;
    }


    // 검색할 음식점 아이디
    private BooleanExpression storeIdEq(UUID storeId, UserRoleEnum role) {
        // CUSTOMER는 storeId로 검색 불가
        if (role == UserRoleEnum.CUSTOMER) {
            return null;
        }
        return storeId != null ? store.id.eq(storeId) : null;
    }


    private BooleanExpression orderStatusIdEq(OrderStatus orderStatus) {
        return orderStatus != null ? order.orderStatus.eq(orderStatus) : null;
    }


    private BooleanExpression orderIdEq(UUID orderId) {
        return orderId != null ? order.id.eq(orderId) : null;
    }





    /**
     * 유효한 페이지 크기 검증 및 설정
     * @param pageSize : 요청된 페이지 크기
     * @return int : 유효한 페이지 크기
     */
    private int validatePageSize(int pageSize) {
        return Set.of(10, 30, 50).contains(pageSize) ? pageSize : 10;
    }


    /**
     * 동적 정렬 조건 생성
     * @param pageable : Pageable 객체에 포함된 정렬 정보
     * @return List<OrderSpecifier<?>> : 동적 정렬 조건 리스트
     */
    private List<OrderSpecifier<?>> dynamicOrder(Pageable pageable) {

        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction
                        = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;

                switch (sortOrder.getProperty()) {
                    case "createdAt":  // 주문 생성일 기준 정렬
                        orderSpecifierList.add(new OrderSpecifier<>(direction, order.createdAt));
                        break;
                    case "updatedAt":  // 주문 업데이트 기준 정렬
                        orderSpecifierList.add(new OrderSpecifier<>(direction, order.updatedAt));
                        break;
                    case "orderStatus":  // 주문 상태 기준 정렬
                        orderSpecifierList.add(new OrderSpecifier<>(direction, order.orderStatus));
                        break;
                    default:  // 잘못된 정렬 필드 처리
                        throw new IllegalArgumentException(
                                "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
                }
            }
        } else {
            // 기본 정렬: 오름차순, createdAt
            orderSpecifierList.add(new OrderSpecifier<>(com.querydsl.core.types.Order.ASC, order.createdAt));
        }
        return orderSpecifierList;
    }
}
