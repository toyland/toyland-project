package com.toyland.payment.model.entity;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.order.model.Order;
import com.toyland.order.model.PaymentType;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

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
    @Column(name = "payment_type")
    private PaymentType paymentType; // 결제유형(카드/현금)

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Builder
    public Payment(UUID id, PaymentType paymentType, Order order) {
        this.id = id;
        this.paymentType = paymentType;
        this.order = order;
    }

    public static Payment of(PaymentRequestDto requestDto, Order order) {
        return Payment.builder()
            .paymentType(requestDto.paymentType())
            .order(order)
            .build();
    }

}
