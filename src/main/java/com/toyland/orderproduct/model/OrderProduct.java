package com.toyland.orderproduct.model;


import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.order.model.Order;
import com.toyland.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
@Table(name = "p_order_product")
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_product_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // p_product 테이블의 product_id로 외래키(FK) 설정
    private Product product;

    @Column(name = "order_product_price", updatable = false, nullable = false)
    private BigDecimal orderProductPrice; // 주문 가격(주문 당시 가격)

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false) // p_order 테이블의 order_id로 외래키(FK) 설정
    private Order order;


    @Builder
    public OrderProduct(Product product, BigDecimal orderProductPrice, Integer quantity) {
        this.product = product;
        this.orderProductPrice = orderProductPrice;
        this.quantity = quantity;
    }


    // 주문 상품 생성 메서드
    public static OrderProduct createOrderProduct(Product product, BigDecimal price,
        Integer quantity) {

        OrderProduct orderProduct = OrderProduct.builder()
            .product(product) // Product 연관 관계 설정
            .orderProductPrice(price)
            .quantity(quantity)
            .build();

        return orderProduct;
    }


    // Order 연결 메서드
    public void associateOrder(Order order) {
        this.order = order; // OrderProduct가 Order를 참조하도록 설정
    }
}
