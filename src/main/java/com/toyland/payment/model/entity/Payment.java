package com.toyland.payment.model.entity;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.order.model.Order;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
@Table(name = "p_payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus; // 결제유형(카드/현금)

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Builder
    public Payment(UUID id, PaymentStatus paymentStatus, Order order) {
        this.id = id;
        this.paymentStatus = paymentStatus;
        this.order = order;
    }

    public static Payment of(PaymentRequestDto requestDto, Order order) {
        return Payment.builder()
            .paymentStatus(requestDto.paymentStatus())
            .order(order)
            .build();
    }

    public void addPayment (PaymentUpdateRequestDto requestDto, Order order) {
        this.order = order;
        this.paymentStatus = requestDto.paymentStatus();
    }

}
