package com.toyland.order.model;

import com.toyland.orderproduct.model.OrderProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus; // 주문상태(주문완료/주문취소/조리중/배달중/배달완료)

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType; // 주문유형(포장/배달)

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_Type")
    private PaymentType paymentType; // 결제유형(카드/현금)

    @OneToMany
    @JoinColumn(name = "order_id") // p_order_product 테이블에 order_id 컬럼으로 외래키 설정
    private List<OrderProduct> orderProductList = new ArrayList<>();

    // 회원 ID (FK, 실제 DB에는 존재하지만 엔티티에는 존재하지 않음)
    // private UUID user_id;
}
