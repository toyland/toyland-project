package com.toyland.order.model;


// 주문유형
public enum OrderType {
    ONLINE_TAKEOUT(Description.ONLINE_TAKEOUT),         // 비대면 - 포장
    ONLINE_DELIVERY(Description.ONLINE_DELIVERY),       // 비대면 - 배달
    OFFLINE_TAKEOUT(Description.OFFLINE_TAKEOUT),       // 대면 - 포장
    OFFLINE_EAT(Description.OFFLINE_EAT);               // 대면 - 가게 식사


    private final String description;


    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String ONLINE_TAKEOUT = "ONLINE_TAKEOUT";
        public static final String ONLINE_DELIVERY = "ONLINE_DELIVERY";
        public static final String OFFLINE_TAKEOUT = "OFFLINE_TAKEOUT";
        public static final String OFFLINE_EAT = "OFFLINE_EAT";
    }
}
