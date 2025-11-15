package com.mk.www.smsmonitor.presentation.dto;

import com.mk.www.smsmonitor.domain.model.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String vendor;
    private LocalDateTime transactionTime;
    private String categoryName;
    private boolean isStupidCost;
    private String memo;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .vendor(transaction.getVendor())
                .transactionTime(transaction.getTransactionTime())
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : null)
                .isStupidCost(transaction.isStupidCost())
                .memo(transaction.getMemo())
                .build();
    }
}
