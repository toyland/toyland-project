package com.toyland.order.model;


// 주문유형(포장/배달)
public enum OrderType {
    TAKEOUT(Description.TAKEOUT),
    DELIVERY(Description.DELIVERY);

    private final String description;


    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String TAKEOUT = "TAKEOUT";
        public static final String DELIVERY = "DELIVERY";
    }
}
