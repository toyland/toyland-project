package com.toyland.ai.infrastructure.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.ai.infrastructure.custom.QnaRepositoryCustom;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

//    @Override
//    public Page<Qna> searchQna(UUID storeId, Pageable pageable) {
//
//        List<Qna> contents = queryFactory
//            .selectFrom(qna)
//            .where(qna.store.id.eq(storeId))
//            .offset(pageable.getOffset())
//            .limit(pageable.getPageSize() + 1)
//            // .orderBy(qna.createdDate.desc())
//            .fetch();
//
//        // 전체 개수 조회
//        long total = queryFactory
//            .select(qna.count())
//            .from(qna)
//            .where(qna.store.id.eq(storeId))
//            .fetchOne();
//
//        return new PageImpl<>(contents, pageable, total);
//    }
}
