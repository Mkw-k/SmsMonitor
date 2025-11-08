package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;

import java.util.List;

public interface OrderRepository {
    List<Order> findPendingOrders();
    OrderJpaEntity save(Order order);
}