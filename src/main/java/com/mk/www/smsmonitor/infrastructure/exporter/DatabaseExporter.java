package com.mk.www.smsmonitor.infrastructure.exporter;

import com.mk.www.smsmonitor.application.port.out.DataExporter;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseExporter implements DataExporter {

    private final TransactionRepository transactionRepository;

    @Override
    public void export(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
