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
        //entity.setId(Long.parseLong(domain.getId()));
        return new OrderJpaEntity(domain.getCustomerName(), domain.getPrice(), domain.getStatus());
    }
}