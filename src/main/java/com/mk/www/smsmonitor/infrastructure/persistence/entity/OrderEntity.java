package com.mk.www.smsmonitor.infrastructure.persistence.entity;

import com.mk.www.smsmonitor.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String customerName;

    private int totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String sender;
}