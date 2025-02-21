package com.toyland.payment.model.entity;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.order.model.Order;
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
    private PaymentStatus paymentStatus; // 결제상태(결제전/결제대기/결제완료)

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Builder
    public Payment(UUID id, PaymentStatus paymentStatus, Order order) {
        this.id = id;
        this.paymentStatus = paymentStatus;
        this.order = order;
    }

    public static Payment of(Order order) {
         Payment payment = Payment.builder()
            .paymentStatus(PaymentStatus.PRE_PAYMENT)
            .order(order) // 결제(Payment)에 주문(Order) 연관 관계 설정 (양방향)
            .build();

        // 주문(Order)에 결제(Payment) 연관 관계 설정
        order.addPayment(payment);

        return payment;
    }

    public void addPayment (PaymentUpdateRequestDto requestDto, Order order) {
        this.order = order;
        this.paymentStatus = requestDto.paymentStatus();
    }

}
