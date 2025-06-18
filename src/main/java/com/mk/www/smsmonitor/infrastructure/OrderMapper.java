package com.mk.www.smsmonitor.infrastructure;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.entity.OrderEntity;

public class OrderMapper {
    public static Order toDomain(OrderEntity entity) {
        return new Order(
                String.valueOf(entity.getId()),
                entity.getCustomerName(),
                entity.getTotalAmount(),
                entity.getStatus()
        );
    }

    public static OrderEntity toEntity(Order domain) {
        OrderEntity entity = new OrderEntity();
//        entity.setId(Long.parseLong(domain.getId()));
        entity.setCustomerName(domain.getCustomerName());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}