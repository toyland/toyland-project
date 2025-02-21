/**
 * @Date : 2025. 02. 21.
 * @author : jieun(je-pa)
 */
package com.toyland.product.infrastructure.impl;


import static com.toyland.product.model.entity.QProduct.product;
import static com.toyland.region.model.entity.QRegion.region;
import static com.toyland.store.model.entity.QStore.store;
import static com.toyland.user.model.QUser.user;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.product.infrastructure.JpaProductRepositoryCustom;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.QProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.QProductWithStoreResponseDto_StoreDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RequiredArgsConstructor
public class JpaProductRepositoryCustomImpl implements JpaProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public Page<ProductWithStoreResponseDto> searchProducts(SearchProductRequestDto dto) {
    List<ProductWithStoreResponseDto> resultList = queryFactory
        .select(
            new QProductWithStoreResponseDto(
                product.id,
                product.name,
                product.price,
                product.isDisplay,
                new QProductWithStoreResponseDto_StoreDto(
                    store.id,
                    store.name,
                    store.content,
                    store.address,
                    region.regionName,
                    user.username
                )
            )
        )
        .from(product)
        .innerJoin(product.store, store)
        .innerJoin(store.owner, user)
        .innerJoin(store.region, region)
        .where(
            dto.getContainsName(),
            dto.getEqStoreId(),
            dto.getEqIsDisplay()
        )
        .orderBy(dto.getOrderSpecifiers())
        .offset(dto.getOffset())
        .limit(dto.getValidateSize())
        .fetch();

    Long totalCount = query(Wildcard.count, product.isDisplay.eq(true), product).fetchOne();

    if (totalCount == null) {
      totalCount = 0L;
    }

    return new PageImpl<>(resultList, org.springframework.data.domain.PageRequest.of(dto.getPage(), dto.getValidateSize()), totalCount);
  }

  private <T> JPAQuery<T> query(Expression<T> expr, BooleanExpression expression, EntityPath<?>... args){
    return queryFactory
        .select(expr)
        .from(args)
        .where(expression);
  }
}
