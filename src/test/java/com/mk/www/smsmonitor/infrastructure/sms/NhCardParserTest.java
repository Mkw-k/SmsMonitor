package com.mk.www.smsmonitor.infrastructure.sms;

import com.mk.www.smsmonitor.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NhCardParserTest {

    private NhCardParser parser;

    @BeforeEach
    void setUp() {
        parser = new NhCardParser();
    }

    @Test
    @DisplayName("NH농협카드_승인_SMS_예시를_Transaction_객체로_정확히_변환한다")
    void NH농협카드_승인_SMS_예시를_Transaction_객체로_정확히_변환한다() {
        // given
        String sms = "NH카드2*0* 승인\n" +
                "고*우\n" +
                "10,600원 체크\n" +
                "11/14 12:49\n" +
                "할리스 오류동점";

        // when
        Optional<Transaction> result = parser.parse(sms);

        // then
        assertThat(result).isPresent();
        Transaction transaction = result.get();
        assertThat(transaction.getAmount()).isEqualTo(new BigDecimal("10600"));
        assertThat(transaction.getVendor()).isEqualTo("할리스 오류동점");
        assertThat(transaction.getTransactionTime()).isEqualTo(LocalDateTime.of(LocalDateTime.now().getYear(), Month.NOVEMBER, 14, 12, 49));
    }

    @Test
    @DisplayName("NH농협카드_승인거절_SMS는_파싱하지_않는다")
    void NH농협카드_승인거절_SMS는_파싱하지_않는다() {
        // given
        String sms = "NH카드2*0* 승인거절\n" +
                "제휴서비스대상아님\n" +
                "고*우\n" +
                "25,000원\n" +
                "11/13 00:57\n" +
                "에버랜드리조트";

        // when
        Optional<Transaction> result = parser.parse(sms);

        // then
        assertThat(result).isNotPresent();
    }
}
