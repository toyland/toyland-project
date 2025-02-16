package com.toyland.orderproduct.model;


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

    // 주문 ID (FK, 실제 DB에는 존재하지만 엔티티에는 존재하지 않음)
    // private UUID order_id;
}
