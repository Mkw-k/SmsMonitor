package com.mk.www.smsmonitor.domain;

public class Order {
    private final String idx;
    private final String customerName;
    private final int price;
    private OrderStatus status;
    private String category;
    private String phone;
    private String size;
    private String term;
    private String memo;

    public Order(String id, String customerName, int totalAmount, OrderStatus status) {
        this.idx = id;
        this.customerName = customerName;
        this.price = totalAmount;
        this.status = status;
    }

    public Order(String idx, String customerName, int price, OrderStatus status, String category, String phone, String size, String term, String memo) {
        this.idx = idx;
        this.customerName = customerName;
        this.price = price;
        this.status = status;
        this.category = category;
        this.phone = phone;
        this.size = size;
        this.term = term;
        this.memo = memo;
    }

    public boolean matchesPayment(Payment payment) {
        return this.customerName.equals(payment.getSenderName()) &&
                this.price == payment.getAmount();
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 주문입니다.");
        }
        this.status = OrderStatus.PAID;
    }

    public String getIdx() { return idx; }
    public OrderStatus getStatus() { return status; }
    public String getCustomerName() { return customerName; }
    public int getPrice() { return price; }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
