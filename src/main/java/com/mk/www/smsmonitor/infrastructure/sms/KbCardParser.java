package com.mk.www.smsmonitor.infrastructure.sms;

import com.mk.www.smsmonitor.application.port.in.SmsParser;
import com.mk.www.smsmonitor.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class KbCardParser implements SmsParser {

    private static final Pattern KB_CARD_PATTERN = Pattern.compile(
            "KB국민카드.*?승인.*?\\n(?<amount>[\\d,]+) 원.*?\\n(?<vendor>.*?)\\n.*?승인시간 (?<date>\\d{2}/\\d{2} \\d{2}:\\d{2})",
            Pattern.DOTALL
    );

    @Override
    public Optional<Transaction> parse(String smsContent) {
        if (!supports(smsContent)) {
            return Optional.empty();
        }

        Matcher matcher = KB_CARD_PATTERN.matcher(smsContent);
        if (matcher.find()) {
            try {
                String amountStr = matcher.group("amount").replace(",", "");
                BigDecimal amount = new BigDecimal(amountStr);
                String vendor = matcher.group("vendor").trim();
                String dateTimeStr = matcher.group("date");

                DateTimeFormatter formatter = new java.time.format.DateTimeFormatterBuilder()
                        .appendPattern("MM/dd HH:mm")
                        .parseDefaulting(java.time.temporal.ChronoField.YEAR, LocalDateTime.now().getYear())
                        .toFormatter();
                LocalDateTime transactionTime = LocalDateTime.parse(dateTimeStr, formatter);


                return Optional.of(Transaction.builder()
                        .amount(amount)
                        .vendor(vendor)
                        .transactionTime(transactionTime)
                        .originalSmsContent(smsContent)
                        .isStupidCost(false) //기본은 false
                        .build());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean supports(String smsContent) {
        return smsContent != null && smsContent.contains("KB국민카드") && smsContent.contains("\n승인") && !smsContent.contains("승인거절");
    }
}
