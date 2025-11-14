package com.mk.www.smsmonitor.domain.model;

public class Payment {
    private final String senderName;
    private final int amount;

    public Payment(String senderName, int amount) {
        this.senderName = senderName;
        this.amount = amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getAmount() {
        return amount;
    }
}