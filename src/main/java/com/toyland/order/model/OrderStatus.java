package com.toyland.order.model;

// 시나리오:
// 사용자가 주문생성 시(주문대기) -> 주문수정 api에서 가게 주인이 만들어진 주문을 접수(주문완료)하거나 주문을 취소(주문취소), 주문 생성 후 5분 이내에만 취소 가능

// 주문상태(주문대기/주문완료/주문취소/조리중/배달중/배달완료)
public enum OrderStatus {
    ORDER_PENDING(Description.ORDER_PENDING),
    ORDER_COMPLETED(Description.ORDER_COMPLETED),
    ORDER_CANCELED(Description.ORDER_CANCELED),
    COOK_PREPARING(Description.COOK_PREPARING),
    DELIVERING(Description.DELIVERING),
    DELIVERY_COMPLETED(Description.DELIVERY_COMPLETED);

    private final String description;


    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String ORDER_PENDING = "ORDER_PENDING";
        public static final String ORDER_COMPLETED = "ORDER_COMPLETED";
        public static final String ORDER_CANCELED = "ORDER_CANCELED";
        public static final String COOK_PREPARING = "COOK_PREPARING";
        public static final String DELIVERING = "DELIVERING";
        public static final String DELIVERY_COMPLETED = "DELIVERY_COMPLETED";
    }
}


