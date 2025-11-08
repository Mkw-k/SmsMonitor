package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.domain.Order;
import com.mk.www.smsmonitor.domain.OrderStatus;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;
import com.mk.www.smsmonitor.infrastructure.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.orderJpaRepository = jpaRepository;
    }

    @Override
    public List<Order> findPendingOrders() {
        return orderJpaRepository.findByStatus(OrderStatus.PENDING)
                .stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public OrderJpaEntity save(Order order) {
        OrderJpaEntity entity = OrderMapper.toEntity(order);
        OrderJpaEntity save = orderJpaRepository.save(entity);
        return save;
    }
}