package com.mk.www.smsmonitor.presentation.dto;

public class PaymentRequest {
    private String 번호;
    private String 가격;

    // Getter, Setter
    public String get번호() { return 번호; }
    public void set번호(String 번호) { this.번호 = 번호; }

    public String get가격() { return 가격; }
    public void set가격(String 가격) { this.가격 = 가격; }
}