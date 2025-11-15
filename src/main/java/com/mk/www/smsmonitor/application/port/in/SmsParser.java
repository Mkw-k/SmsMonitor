package com.mk.www.smsmonitor.application.port.in;

import com.mk.www.smsmonitor.domain.model.Transaction;

import java.util.Optional;

public interface SmsParser {
    Optional<Transaction> parse(String smsContent);
    boolean supports(String smsContent);
}
