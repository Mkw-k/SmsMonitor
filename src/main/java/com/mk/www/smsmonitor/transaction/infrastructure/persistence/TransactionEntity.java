package com.mk.www.smsmonitor.transaction.infrastructure.persistence;

import com.mk.www.smsmonitor.category.infrastructure.persistence.SpendingCategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String vendor;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SpendingCategoryEntity category;

    @Column(nullable = false)
    private boolean isStupidCost;

    @Lob
    @Column(nullable = false)
    private String originalSmsContent;

    private String memo;

    private String cardNumber;

    private String name;
}
