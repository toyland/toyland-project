package com.toyland.payment.model.entity;


// 결제유형(카드/현금)
public enum PaymentStatus {
    PRE_PAYMENT(Description.PRE_PAYMENT),
    PENDING(Description.PENDING),
    COMPLETED(Description.COMPLETED);


    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String PRE_PAYMENT = "PRE_PAYMENT";
        public static final String PENDING = "PENDING";
        public static final String COMPLETED = "COMPLETED";
    }
}

