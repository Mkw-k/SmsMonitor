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
public class NhCardParser implements SmsParser {

    // 모든 필드를 다 캡처하도록 수정된 정규식
    private static final Pattern NH_CARD_PATTERN = Pattern.compile(
            "NH카드(?<cardNum>.*?)승인\\n" +        // 1번째 줄: 카드번호 (2*0*) 추출
                    "(?<name>.*?)\\n" +               // 2번째 줄: 사용자명 (고*우) 추출
                    "(?<amount>[\\d,]+)원.*?\\n" +          // 3번째 줄: 금액 추출 (뒤에 '체크' 같은건 무시하거나 필요하면 (?<type>.*?) 추가)
                    "(?<date>\\d{2}/\\d{2} \\d{2}:\\d{2})\\n" + // 4번째 줄: 날짜
                    "(?<vendor>.*)",                      // 5번째 줄: 사용처
            Pattern.DOTALL
    );

    @Override
    public Optional<Transaction> parse(String smsContent) {
        if (!supports(smsContent)) {
            return Optional.empty();
        }

        Matcher matcher = NH_CARD_PATTERN.matcher(smsContent);

        if (matcher.find()) {
            try {
                // 1. 데이터 추출 (그룹 이름으로 가져오기)
                String cardNum = matcher.group("cardNum").trim();
                String name = matcher.group("name").trim();
                String amountStr = matcher.group("amount").replace(",", "");
                String dateTimeStr = matcher.group("date");
                String vendor = matcher.group("vendor").trim();

                // 2. 데이터 변환
                BigDecimal amount = new BigDecimal(amountStr);

                DateTimeFormatter formatter = new java.time.format.DateTimeFormatterBuilder()
                        .appendPattern("MM/dd HH:mm")
                        .parseDefaulting(java.time.temporal.ChronoField.YEAR, LocalDateTime.now().getYear())
                        .toFormatter();
                LocalDateTime transactionTime = LocalDateTime.parse(dateTimeStr, formatter);

                // 3. 객체 생성
                return Optional.of(Transaction.builder()
                        .cardNumber(cardNum)    // Transaction 모델에 이 필드 추가 필요
                        .name(name)     // Transaction 모델에 이 필드 추가 필요
                        .amount(amount)
                        .vendor(vendor)
                        .transactionTime(transactionTime)
                        .originalSmsContent(smsContent)
                        .isStupidCost(false)
                        .build());

            } catch (Exception e) {
                log.error("NH카드 SMS 파싱 에러: {}", e.getMessage(), e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean supports(String smsContent) {
        return smsContent != null && smsContent.contains("NH카드") && smsContent.contains("승인") && !smsContent.contains("승인거절");
    }
}
