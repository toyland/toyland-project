package com.toyland.order.model;


// 주문상태(주문완료/주문취소/조리중/배달중/배달완료)
public enum OrderStatus {
    ORDER_COMPLETED(Description.ORDER_COMPLETED),
    ORDER_CANCELED(Description.ORDER_CANCELED),
    PREPARING(Description.PREPARING),
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
        public static final String ORDER_COMPLETED = "ORDER_COMPLETED";
        public static final String ORDER_CANCELED = "ORDER_CANCELED";
        public static final String PREPARING = "PREPARING";
        public static final String DELIVERING = "DELIVERING";
        public static final String DELIVERY_COMPLETED = "DELIVERY_COMPLETED";
    }
}


