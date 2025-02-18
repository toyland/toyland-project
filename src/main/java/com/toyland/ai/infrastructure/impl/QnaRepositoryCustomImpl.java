package com.toyland.ai.infrastructure.impl;


import static com.toyland.ai.model.QQna.qna;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.ai.infrastructure.QnaRepositoryCustom;
import com.toyland.ai.model.QQna;
import com.toyland.ai.model.Qna;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Qna> searchQna(UUID storeId, Pageable pageable) {
    List<Qna> contents = queryFactory
        .selectFrom(qna)
        .where(qna.store.id.eq(storeId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .orderBy(qna.createdAt.desc())
        .fetch();

    // 전체 개수 조회
    long total = queryFactory
        .select(QQna.qna.count())
        .from(QQna.qna)
        .where(QQna.qna.store.id.eq(storeId))
        .fetchOne();

    return new PageImpl<>(contents, pageable, total);
  }


}
