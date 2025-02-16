package com.toyland.orderproduct.model;


import com.toyland.order.model.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_product_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_product_price", updatable = false, nullable = false)
    private String orderProductPrice; // 주문 가격(주문 당시 가격)

    @Column(name = "content", nullable = false)
    private String quantity;

    @ManyToOne
    @JoinColumn(name = "order_id") // p_order 테이블의 order_id로 외래키(FK) 설정
    private Order order;
}
