package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.domain.model.Order;
import com.mk.www.smsmonitor.domain.model.OrderStatus;
import com.mk.www.smsmonitor.domain.repository.OrderRepository;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;
import com.mk.www.smsmonitor.infrastructure.persistence.mapper.OrderMapper;
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