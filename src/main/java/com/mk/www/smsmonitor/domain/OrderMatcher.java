package com.mk.www.smsmonitor.domain;


import java.util.List;
import java.util.Optional;

public class OrderMatcher {
    private final List<Order> pendingOrders;

    public OrderMatcher(List<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Optional<Order> match(Payment payment) {
        return pendingOrders.stream()
                .filter(order -> order.matchesPayment(payment))
                .findFirst();
    }
}