package com.awbd.ecommerce.model;

public enum PaymentMethod {
    CREDIT_CARD("CREDIT CARD"),
    DEBIT_CARD("DEBIT CARD"),
    PAYPAL("PAYPAL"),
    BANK_TRANSFER("BANK TRANSFER"),
    CASH_ON_DELIVERY("CASH ON DELIVERY");

    private String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
