package com.mk.www.smsmonitor.infrastructure.googlesheet;

import com.mk.www.smsmonitor.domain.model.Order;
import com.mk.www.smsmonitor.domain.model.OrderStatus;
import com.mk.www.smsmonitor.domain.repository.OrderRepository;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;

// 실제 구글 시트 API 연동 필요
public class GoogleSheetOrderRepository implements OrderRepository {

    private final GoogleSheetClient client;

    public GoogleSheetOrderRepository(GoogleSheetClient client) {
        this.client = client;
    }

    @Override
    public List<Order> findPendingOrders() {
        return client.readAllOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public OrderEntity save(Order order) {
        client.updateOrderStatus(order.getId(), order.getStatus());
        return null;
    }
}