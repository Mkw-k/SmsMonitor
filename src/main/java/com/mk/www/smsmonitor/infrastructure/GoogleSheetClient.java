package com.mk.www.smsmonitor.infrastructure;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.domain.OrderStatus;

import java.util.List;

public interface GoogleSheetClient {
    List<Order> readAllOrders();
    void updateOrderStatus(String orderId, OrderStatus status);
}
