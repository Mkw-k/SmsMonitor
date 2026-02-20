package com.mk.www.smsmonitor.transaction.domain;

import com.mk.www.smsmonitor.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
    Page<Transaction> findAll(Pageable pageable);
    Page<Transaction> findAllByIsStupidCost(boolean isStupidCost, Pageable pageable);
}
