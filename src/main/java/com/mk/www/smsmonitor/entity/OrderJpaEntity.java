package com.mk.www.smsmonitor.entity;

import com.mk.www.smsmonitor.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
public class OrderJpaEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String customerName;

    private int totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String sender;

    public OrderJpaEntity(String customerName, OrderStatus status, String sender) {
        this.customerName = customerName;
        this.status = status;
        this.sender = sender;
    }

    public OrderJpaEntity(String customerName, int totalAmount, OrderStatus status) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public OrderJpaEntity() {
    }
}