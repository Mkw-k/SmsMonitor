package com.mk.www.smsmonitor.infrastructure;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;

public class OrderMapper {
    public static Order toDomain(OrderJpaEntity entity) {
        return new Order(
                String.valueOf(entity.getId()),
                entity.getCustomerName(),
                entity.getTotalAmount(),
                entity.getStatus()
        );
    }

    public static OrderJpaEntity toEntity(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
//        entity.setId(Long.parseLong(domain.getId()));
        entity.setCustomerName(domain.getCustomerName());
        entity.setTotalAmount(domain.getPrice());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}