package com.mk.www.smsmonitor.infrastructure.googlesheet;

import com.mk.www.smsmonitor.domain.model.Order;
import com.mk.www.smsmonitor.domain.model.OrderStatus;

import java.util.List;

public interface GoogleSheetClient {
    List<Order> readAllOrders();
    void updateOrderStatus(String orderId, OrderStatus status);
}
