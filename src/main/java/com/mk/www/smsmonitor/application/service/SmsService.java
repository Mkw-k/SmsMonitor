package com.mk.www.smsmonitor.application.service;

import com.mk.www.smsmonitor.application.port.in.SmsParser;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.service.StupidCostStrategy;
import com.mk.www.smsmonitor.presentation.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SmsService {

    private final TransactionService transactionService;
    private final List<StupidCostStrategy> stupidCostStrategies;
    private final List<SmsParser> parsers;

    public boolean processNewSms(SmsRequest request) {
        Optional<Transaction> transactionOptional = parseSms(request.getMessage());

        log.info("sms 전문 >>> {}", request.getMessage());

        if (transactionOptional.isEmpty()) {
            return false;
        }

        Transaction transaction = transactionOptional.get();
        transaction.analyze(stupidCostStrategies);
        transactionService.save(transaction);

        return true;
    }

    private Optional<Transaction> parseSms(String smsContent) {
        for (SmsParser parser : parsers) {
            if (parser.supports(smsContent)) {
                return parser.parse(smsContent);
            }
        }
        return Optional.empty();
    }
}
