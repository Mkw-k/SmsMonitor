package com.mk.www.smsmonitor.transaction.domain;

import com.mk.www.smsmonitor.category.domain.SpendingCategory;
import com.mk.www.smsmonitor.transaction.domain.StupidCostStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Transaction {
    private Long id;
    private BigDecimal amount;
    private String vendor;
    private LocalDateTime transactionTime;
    private SpendingCategory category;
    private boolean isStupidCost;
    private String originalSmsContent;
    private String memo;
    private String cardNumber;
    private String name;

    //해당 거래내역을 멍청비용으로 변경
    public void classifyAsStupidCost() {
        this.isStupidCost = true;
    }

    //해당 거래내역의 메모를 업데이트함
    public void updateMemo(String memo) {
        this.memo = memo;
    }

    //해당 내역이 멍청비용이 맞는지 분별
    public void analyze(List<StupidCostStrategy> strategies) {
        for (StupidCostStrategy strategy : strategies) {
            if (strategy.isStupidCost(this)) {
                this.classifyAsStupidCost();
                break;
            }
        }
    }
}
