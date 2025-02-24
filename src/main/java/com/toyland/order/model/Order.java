package com.toyland.order.model;

import com.toyland.address.model.entity.Address;
import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.OrderErrorCode;
import com.toyland.order.presentation.dto.CreateOrderRequestDto;
import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.payment.model.entity.Payment;
import com.toyland.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Column(name = "order_request", length = 255)
    private String orderRequest; // 요청 사항

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType; // 결제유형(카드/현금)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="address_id", nullable = false)
    private Address address;

    @Column(name = "address_detail", nullable = false, length = 100)
    private String addressDetail; // 상세 주소

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // p_user 테이블의 user_id를 FK로 설정
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();


    @Builder
    public Order(User user, Address address, String addressDetail, PaymentType paymentType, OrderType orderType, OrderStatus orderStatus, String orderRequest) {
        this.user = user;
        this.paymentType = paymentType;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.orderRequest = orderRequest;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    //연관관계 편의 메소드 (테스트용)
    public void joinUser(User user) {
        this.user = user;
        user.getOrderList().add(this);
    }

    // 주문 생성 메서드
    public static Order createOrder(User user, Address address, CreateOrderRequestDto dto,
        List<OrderProduct> orderProductList) {

        Order order = Order.builder()
            .user(user) // user 연관 관계 자동 설정
            .address(address)
            .addressDetail(dto.getAddressDetail())
            .orderType(dto.getOrderType())
            .paymentType(dto.getPaymentType())
            .orderStatus(OrderStatus.ORDER_PENDING)
            .orderRequest(dto.getOrderRequest())
            .build();

        // 주문 상품(OrderProduct)과 주문(Order) 간의 연관 관계 설정
        for (OrderProduct orderProduct : orderProductList) {
            order.addOrderProduct(orderProduct);
        }
        return order;
    }




    // 비즈니스 로직
    /**
     *  주문 수정 (주문 사항 변경)
     */
    public void update(CreateOrderRequestDto createOrderRequestDto, List<OrderProduct> orderProductList, Address address) {
        // 상태 변경 가능 여부 확인
        if (this.orderStatus != OrderStatus.ORDER_PENDING) {
            throw CustomException.from(OrderErrorCode.INVALID_STATUS);
        }

        // 주문 생성 후 5분 이내에만 변경 가능
        if (!isCancelUpdate()) {
            throw new CustomException(OrderErrorCode.CANCEL_UPDATE_TIME_EXCEEDED);
        }

        // 주문 업데이트 로직 (주문자, 주문상태는 변경 불가능)
        this.orderType = createOrderRequestDto.getOrderType();
        this.paymentType = createOrderRequestDto.getPaymentType();
        this.orderRequest = createOrderRequestDto.getOrderRequest();
        this.addressDetail = createOrderRequestDto.getAddressDetail();
        this.address = address;


        // 주문 상품 업데이트 로직 (기존 상품 삭제 후 추가)
        this.orderProductList.clear();
        for (OrderProduct orderProduct : orderProductList) {
            this.addOrderProduct(orderProduct);
        }
    }


    // 연관 관계 설정 메서드
    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProductList.add(orderProduct);  // 주문 상품 리스트에 추가
        orderProduct.associateOrder(
            this); // OrderProduct에도 해당 Order 정보 설정 (양방향 관계 유지), setter 대신 associateOrder 메서드 사용
    }


    public void addPayment(Payment payment){
        this.payment = payment;
    }



    /**
     * 주문 수정 (주문 처리)
     */
    public void process(OrderStatus newStatus) {
        // 상태 변경 가능 여부 확인
        if (this.orderStatus != OrderStatus.ORDER_PENDING) {
            throw CustomException.from(OrderErrorCode.INVALID_STATUS);
        }

        // 주문 처리
        if (newStatus == OrderStatus.ORDER_CANCELED) {
            // 주문 취소
            cancel();
        } else if(newStatus == OrderStatus.ORDER_COMPLETED) {
            // 주문 접수 (주문 완료)
            this.orderStatus = newStatus;
        } else {
            throw CustomException.from(OrderErrorCode.INVALID_STATUS);
        }

    }


    // 주문 취소
    public void cancel() {
        // 주문 생성 후 5분 이내에만 취소 가능
        if (!isCancelUpdate()) {
            throw new CustomException(OrderErrorCode.CANCEL_UPDATE_TIME_EXCEEDED);
        }

        // 주문 취소 상태로 수정
        this.orderStatus = OrderStatus.ORDER_CANCELED;
    }

    // 주문 취소 및 변경 가능 여부 확인주문, 생성 시간 (createdAt) 기준으로 5분 이내에만 취소 가능
    public boolean isCancelUpdate() {
        LocalDateTime createdTime = this.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, now);
        // 주문 생성 후 5분 이내인지 확인
        return duration.toMinutes() < 5;
    }



    /**
     * 주문 삭제
     */
    public void deleteOrder(Long loginUserId) {
        // 주문 논리적 삭제 처리
        this.addDeletedField(loginUserId);

        // 주문 상품 논리적 삭제 처리
        for (OrderProduct orderProduct : orderProductList) {
            orderProduct.addDeletedField(loginUserId); // OrderProduct에도 삭제 정보 추가
        }
    }
}
