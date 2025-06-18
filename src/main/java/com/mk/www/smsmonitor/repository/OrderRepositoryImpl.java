package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.domain.OrderStatus;
import com.mk.www.smsmonitor.entity.OrderEntity;
import com.mk.www.smsmonitor.infrastructure.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Order> findPendingOrders() {
        return jpaRepository.findByStatus(OrderStatus.PENDING)
                .stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public OrderEntity save(Order order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        OrderEntity save = jpaRepository.save(entity);
        return save;
    }
}