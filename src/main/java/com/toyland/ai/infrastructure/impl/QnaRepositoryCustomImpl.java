package com.toyland.ai.infrastructure.impl;


import static com.toyland.ai.model.QQna.qna;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.ai.infrastructure.QnaRepositoryCustom;
import com.toyland.ai.model.Qna;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Qna> searchQna(UUID storeId, Pageable pageable) {

    int pageSize = validatePageSize(pageable.getPageSize());

    List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

    List<Qna> fetch = query(qna, storeId)
        .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageSize)
        .fetch();

    Long totalCount = query(Wildcard.count, storeId).fetchOne();

    if (totalCount == null) {
      totalCount = 0L;
    }

    return new PageImpl<>(fetch, pageable, totalCount);
  }

  private int validatePageSize(int pageSize) {
    return Set.of(10, 30, 50).contains(pageSize) ? pageSize : 10;
  }


  private <T> JPAQuery<T> query(Expression<T> expr, UUID storeId) {
    return queryFactory
        .select(expr)
        .from(qna)
        .where(
            storeIdContains(storeId)
        );
  }

  private BooleanExpression storeIdContains(UUID storeId) {
    return storeId != null ? qna.store.id.eq(storeId) : null;
  }

  private List<OrderSpecifier<?>> dynamicOrder(Pageable pageable) {
    List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

    if (pageable.getSort() != null) {
      for (Sort.Order sortOrder : pageable.getSort()) {
        Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

        switch (sortOrder.getProperty()) {
          case "createdAt":
            orderSpecifierList.add(new OrderSpecifier<>(direction, qna.createdAt));
            break;
          case "updatedAt":
            orderSpecifierList.add(new OrderSpecifier<>(direction, qna.updatedAt));
            break;
          default:
            throw new IllegalArgumentException(
                "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
        }
      }
    } else {
      orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, qna.createdAt));
    }
    return orderSpecifierList;

  }


}
