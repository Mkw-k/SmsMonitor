package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.entity.OrderEntity;

import java.util.List;

public interface OrderRepository {
    List<Order> findPendingOrders();
    OrderEntity save(Order order);
}