package com.mk.www.smsmonitor.transaction.infrastructure.persistence;

import com.mk.www.smsmonitor.transaction.domain.Transaction;
import com.mk.www.smsmonitor.transaction.domain.TransactionRepository;
import com.mk.www.smsmonitor.transaction.infrastructure.persistence.TransactionEntity;
import com.mk.www.smsmonitor.transaction.domain.Transaction;
import com.mk.www.smsmonitor.transaction.infrastructure.persistence.TransactionEntity;
import com.mk.www.smsmonitor.transaction.infrastructure.persistence.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        TransactionEntity savedEntity = transactionJpaRepository.save(entity);
        return transactionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionJpaRepository.findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return transactionJpaRepository.findAll(pageable)
                .map(transactionMapper::toDomain);
    }

    @Override
    public Page<Transaction> findAllByIsStupidCost(boolean isStupidCost, Pageable pageable) {
        return transactionJpaRepository.findAllByIsStupidCost(isStupidCost, pageable)
                .map(transactionMapper::toDomain);
    }
}
