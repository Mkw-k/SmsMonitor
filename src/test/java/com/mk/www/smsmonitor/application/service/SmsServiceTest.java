package com.mk.www.smsmonitor.application.service;

import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.service.StupidCostStrategy;
import com.mk.www.smsmonitor.infrastructure.sms.KbCardParser;
import com.mk.www.smsmonitor.infrastructure.sms.NhCardParser;
import com.mk.www.smsmonitor.presentation.dto.SmsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    private SmsService smsService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private List<StupidCostStrategy> stupidCostStrategies;

    @Mock
    private KbCardParser kbCardParser;

    @Mock
    private NhCardParser nhCardParser;

    @BeforeEach
    void setUp() {
        smsService = new SmsService(transactionService, stupidCostStrategies, List.of(kbCardParser, nhCardParser));
    }

    @Test
    @DisplayName("SMS_처리_성공_시_파싱_분석_저장_서비스를_모두_호출한다")
    void SMS_처리_성공_시_파싱_분석_저장_서비스를_모두_호출한다() {
        // given
        String smsContent = "SMS content";
        SmsRequest request = new SmsRequest();
        request.setMessage(smsContent);
        Transaction mockTransaction = mock(Transaction.class);

        when(kbCardParser.supports(smsContent)).thenReturn(true);
        when(kbCardParser.parse(smsContent)).thenReturn(Optional.of(mockTransaction));

        // when
        boolean result = smsService.processNewSms(request);

        // then
        assertThat(result).isTrue();
        verify(kbCardParser, times(1)).supports(smsContent);
        verify(kbCardParser, times(1)).parse(smsContent);
        verify(mockTransaction, times(1)).analyze(stupidCostStrategies);
        verify(transactionService, times(1)).save(mockTransaction);
    }

    @Test
    @DisplayName("SMS_파싱_실패_시_다른_서비스는_호출되지_않는다")
    void SMS_파싱_실패_시_다른_서비스는_호출되지_않는다() {
        // given
        String smsContent = "Failed SMS content";
        SmsRequest request = new SmsRequest();
        request.setMessage(smsContent);

        when(kbCardParser.supports(smsContent)).thenReturn(false);
        when(nhCardParser.supports(smsContent)).thenReturn(false);

        // when
        boolean result = smsService.processNewSms(request);

        // then
        assertThat(result).isFalse();
        verify(transactionService, never()).save(any());
    }

    @Test
    @DisplayName("NH카드_체크카드_승인_SMS_처리_성공")
    void NH카드_체크카드_승인_SMS_처리_성공() {
        // given
        String smsContent = "NH카드2*0*승인\n고*우\n1,000원 체크\n11/22 16:25\n지에스(GS)25 궁동중앙점";
        SmsRequest request = new SmsRequest();
        request.setSender("+821012345678");
        request.setMessage(smsContent);

        // when
        boolean result = smsService.processNewSms(request);

        // then
        assertThat(result).isTrue();
        verify(kbCardParser, times(1)).supports(smsContent);
        verify(kbCardParser, times(1)).parse(smsContent);
    }
}
