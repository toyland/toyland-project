package com.toyland.review.infrastructure.impl;


import static com.toyland.review.model.QReview.review;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.review.infrastructure.ReviewRepositoryCustom;
import com.toyland.review.model.Review;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Review> searchReviews(UUID storeId, Pageable pageable) {
    List<Review> contents = queryFactory
        .selectFrom(review)
        .where(review.store.id.eq(storeId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .orderBy(review.createdAt.desc())
        .fetch();

    // 전체 개수 조회
    long total = queryFactory
        .select(review.count())
        .from(review)
        .where(review.store.id.eq(storeId))
        .fetchOne();

    return new PageImpl<>(contents, pageable, total);
  }


  @Override
  public Optional<List<Review>> getReviewList(UUID storeId) {

    List<Review> fetch = query(review, storeId).fetch();
    return Optional.ofNullable(fetch);

  }

  private <T> JPAQuery<T> query(Expression<T> expr, UUID storeId) {
    return queryFactory
        .select(expr)
        .from(review)
        .where(review.store.id.eq(storeId));
  }


}
