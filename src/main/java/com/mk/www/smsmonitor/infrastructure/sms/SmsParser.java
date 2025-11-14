package com.mk.www.smsmonitor.infrastructure.sms;

import com.mk.www.smsmonitor.domain.model.Payment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SmsParser {
    public Optional<Payment> parse(String smsContent) {
        // 예시: "[Web발신] 하나은행 10000원 입금 홍길동"
        try {
            String[] parts = smsContent.split(" ");
            int amount = Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
            String senderName = parts[4];
            return Optional.of(new Payment(senderName, amount));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}