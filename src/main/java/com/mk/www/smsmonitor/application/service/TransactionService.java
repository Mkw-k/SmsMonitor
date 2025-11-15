package com.mk.www.smsmonitor.application.service;


import com.mk.www.smsmonitor.application.port.out.DataExporter;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.repository.TransactionRepository;
import com.mk.www.smsmonitor.presentation.dto.MemoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private final List<DataExporter> exporters;
    private final TransactionRepository transactionRepository;

    public TransactionService(List<DataExporter> exporters, TransactionRepository transactionRepository) {
        this.exporters = exporters;
        this.transactionRepository = transactionRepository;
    }

    public Transaction save(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        for (DataExporter exporter : exporters) {
            exporter.export(savedTransaction);
        }
        return savedTransaction;
    }

    public Optional<Transaction> updateMemo(Long id, MemoRequest request) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.updateMemo(request.getMemo());
                    return transactionRepository.save(transaction);
                });
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getStupidCostTransactions(Pageable pageable) {
        return transactionRepository.findAllByIsStupidCost(true, pageable);
    }
}
