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

class KbCardParserTest {

    private KbCardParser parser;

    @BeforeEach
    void setUp() {
        parser = new KbCardParser();
    }

    @Test
    @DisplayName("KB국민카드_승인_SMS_예시를_Transaction_객체로_정확히_변환한다")
    void KB국민카드_승인_SMS_예시를_Transaction_객체로_정확히_변환한다() {
        // given
        String sms = "KB국민카드4020\n" +
                "승인\n" +
                "10,000 원(일시불)\n" +
                "서대문 김치찜 앤 찜\n" +
                "고객명 고*우님\n" +
                "승인시간 11/04 12:34\n" +
                "누적 10,000원";

        // when
        Optional<Transaction> result = parser.parse(sms);

        // then
        assertThat(result).isPresent();
        Transaction transaction = result.get();
        assertThat(transaction.getAmount()).isEqualTo(new BigDecimal("10000"));
        assertThat(transaction.getVendor()).isEqualTo("서대문 김치찜 앤 찜");
        assertThat(transaction.getTransactionTime()).isEqualTo(LocalDateTime.of(LocalDateTime.now().getYear(), Month.NOVEMBER, 4, 12, 34));
    }

    @Test
    @DisplayName("지원하지_않는_SMS_형식은_파싱하지_않는다")
    void 지원하지_않는_SMS_형식은_파싱하지_않는다() {
        // given
        String sms = "다른 카드사 문자 메시지";

        // when
        Optional<Transaction> result = parser.parse(sms);

        // then
        assertThat(result).isNotPresent();
    }
}
