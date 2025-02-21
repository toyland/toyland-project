package com.toyland.payment.infrastructure.Impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.payment.infrastructure.JpaPaymentRepositoryCustom;
import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.entity.PaymentStatus;
import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
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

import static com.toyland.payment.model.entity.QPayment.payment;


@RequiredArgsConstructor
@Slf4j
public class JpaPaymentRepositoryCustomImpl implements JpaPaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PaymentSearchResponseDto> searchPayment(PaymentSearchRequestDto searchRequestDto, Pageable pageable) {

        List<OrderSpecifier<?>> orderSpecifierList = dynamicOrder(pageable);

        int pageSize = validatePageSize(pageable.getPageSize());

        List<Payment> fetch = query(payment, searchRequestDto)
                .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageSize)
                .distinct()
                .fetch();

        // PaymentSearchResponseDto로 변환
        List<PaymentSearchResponseDto> paymentSearchResponseDtos = fetch.stream()
                .map(PaymentSearchResponseDto::from)
                .collect(Collectors.toList());

        Long totalCount = query(Wildcard.count, searchRequestDto).fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(paymentSearchResponseDtos, pageable, totalCount);

    }

    private <T> JPAQuery<T> query(Expression<T> expr, PaymentSearchRequestDto searchRequestDto) {
        return queryFactory
                .select(expr)
                .from(payment)
                .where(
                        paymentIdEq(searchRequestDto.paymentId()),
                        paymentStatusEq(searchRequestDto.paymentStatus())
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
                        orderSpecifierList.add(new OrderSpecifier<>(direction, payment.createdAt));
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "잘못된 정렬 필드입니다. : " + sortOrder.getProperty());
                }

            }
        } else {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, payment.createdAt));
        }
        return orderSpecifierList;
    }



    private BooleanExpression paymentStatusEq(PaymentStatus paymentStatus) {
        return paymentStatus != null ? payment.paymentStatus.eq(paymentStatus) : null;
    }


    private BooleanExpression paymentIdEq(UUID paymentId) {
        return paymentId != null ? payment.id.eq(paymentId) : null;
    }


}