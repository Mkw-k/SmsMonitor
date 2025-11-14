package com.mk.www.smsmonitor.domain.repository;

import com.mk.www.smsmonitor.domain.model.Order;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;

import java.util.List;

public interface OrderRepository {
    List<Order> findPendingOrders();
    OrderEntity save(Order order);
}