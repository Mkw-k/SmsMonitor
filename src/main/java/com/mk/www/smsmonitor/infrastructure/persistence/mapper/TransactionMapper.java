package com.mk.www.smsmonitor.infrastructure.persistence.mapper;

import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final SpendingCategoryMapper spendingCategoryMapper;

    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return Transaction.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .vendor(entity.getVendor())
                .transactionTime(entity.getTransactionTime())
                .category(spendingCategoryMapper.toDomain(entity.getCategory()))
                .isStupidCost(entity.isStupidCost())
                .originalSmsContent(entity.getOriginalSmsContent())
                .memo(entity.getMemo())
                .build();
    }

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setVendor(domain.getVendor());
        entity.setTransactionTime(domain.getTransactionTime());
        entity.setCategory(spendingCategoryMapper.toEntity(domain.getCategory()));
        entity.setStupidCost(domain.isStupidCost());
        entity.setOriginalSmsContent(domain.getOriginalSmsContent());
        entity.setMemo(domain.getMemo());
        entity.setName(domain.getName());
        entity.setCardNumber(domain.getCardNumber());

        return entity;
    }
}
