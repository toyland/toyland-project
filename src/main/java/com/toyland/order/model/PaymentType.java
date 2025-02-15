package com.toyland.order.model;


// 결제유형(카드/현금)
public enum PaymentType {
    CARD(Description.CARD),
    CASH(Description.CASH);

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String CARD = "CARD";
        public static final String CASH = "CASH";
    }
}

