package com.toyland.order.model;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.OrderErrorCode;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
@Table(name = "p_order")
public class Order extends BaseEntity {

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
    @Column(name = "payment_type")
    private PaymentType paymentType; // 결제유형(카드/현금)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // p_user 테이블의 user_id를 FK로 설정
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();


    @Builder
    public Order(User user, PaymentType paymentType, OrderType orderType, OrderStatus orderStatus) {
        this.user = user;
        this.paymentType = paymentType;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }

    //연관관계 편의 메소드 (테스트용)
    public void joinUser(User user) {
        this.user = user;
        user.getOrderList().add(this);
    }

    // 주문 생성 메서드
    public static Order createOrder(User user, CreateOrderRequestDto dto, List<OrderProduct> orderProductList) {

        Order order = Order.builder()
            .user(user) // user 연관 관계 자동 설정
            .orderType(dto.getOrderType())
            .paymentType(dto.getPaymentType())
            .orderStatus(OrderStatus.ORDER_COMPLETED)
            .build();

        // 주문 상품(OrderProduct)과 주문(Order) 간의 연관 관계 설정
        for (OrderProduct orderProduct : orderProductList) {
            order.addOrderProduct(orderProduct);
        }
        return order;
    }


    // 연관 관계 설정 메서드
    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProductList.add(orderProduct);  // 주문 상품 리스트에 추가
        orderProduct.associateOrder(this); // OrderProduct에도 해당 Order 정보 설정 (양방향 관계 유지), setter 대신 associateOrder 메서드 사용
    }


    // 비즈니스 로직
    /**
     * 주문 삭제(취소)
     */
    public void cancel() {
        if (isAvailableCancelStatus()) {
            throw new CustomException(OrderErrorCode.INVALID_STATUS);
        }

        // 후에 결제 취소 로직 추가

        // 주문 취소 상태로 수정
        this.orderStatus = OrderStatus.ORDER_CANCELED;

        // 주문 논리적 삭제 처리
        this.addDeletedField(user.getId());

        // 주문 상품 논리적 삭제 처리
        for(OrderProduct orderProduct : orderProductList) {
            orderProduct.addDeletedField(user.getId()); // OrderProduct에도 삭제 정보 추가
        }
    }

    private boolean isAvailableCancelStatus() {
        return this.orderStatus == OrderStatus.PREPARING || this.orderStatus == OrderStatus.DELIVERING || this.orderStatus == OrderStatus.DELIVERY_COMPLETED;
    }
}
