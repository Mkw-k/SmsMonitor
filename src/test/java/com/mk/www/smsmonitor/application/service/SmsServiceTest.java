package com.mk.www.smsmonitor.application.service;

import com.mk.www.smsmonitor.application.port.in.SmsParser;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.service.StupidCostStrategy;
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
    private SmsParser parser1;

    @Mock
    private SmsParser parser2;

    @BeforeEach
    void setUp() {
        smsService = new SmsService(transactionService, stupidCostStrategies, List.of(parser1, parser2));
    }

    @Test
    @DisplayName("SMS_처리_성공_시_파싱_분석_저장_서비스를_모두_호출한다")
    void SMS_처리_성공_시_파싱_분석_저장_서비스를_모두_호출한다() {
        // given
        String smsContent = "SMS content";
        SmsRequest request = new SmsRequest();
        request.setMessage(smsContent);
        Transaction mockTransaction = mock(Transaction.class);

        when(parser1.supports(smsContent)).thenReturn(true);
        when(parser1.parse(smsContent)).thenReturn(Optional.of(mockTransaction));

        // when
        boolean result = smsService.processNewSms(request);

        // then
        assertThat(result).isTrue();
        verify(parser1, times(1)).supports(smsContent);
        verify(parser1, times(1)).parse(smsContent);
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

        when(parser1.supports(smsContent)).thenReturn(false);
        when(parser2.supports(smsContent)).thenReturn(false);

        // when
        boolean result = smsService.processNewSms(request);

        // then
        assertThat(result).isFalse();
        verify(transactionService, never()).save(any());
    }
}
