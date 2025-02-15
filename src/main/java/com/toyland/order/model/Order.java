package com.toyland.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "p_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus order_status; // 주문상태(주문완료/주문취소/조리중/배달중/배달완료)

    @Enumerated(value = EnumType.STRING)
    private OrderType order_type; // 주문유형(포장/배달)

    @Enumerated(value = EnumType.STRING)
    private PaymentType payment_type; // 결제유형(카드/현금)
}
