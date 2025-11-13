package com.mk.www.smsmonitor.domain.model;

public class Order {
    private final String id;
    private final String customerName;
    private final int totalAmount;
    private OrderStatus status;

    public Order(String id, String customerName, int totalAmount, OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public boolean matchesPayment(Payment payment) {
        return this.customerName.equals(payment.getSenderName()) &&
                this.totalAmount == payment.getAmount();
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 주문입니다.");
        }
        this.status = OrderStatus.PAID;
    }

    public String getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public String getCustomerName() { return customerName; }
    public int getTotalAmount() { return totalAmount; }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
