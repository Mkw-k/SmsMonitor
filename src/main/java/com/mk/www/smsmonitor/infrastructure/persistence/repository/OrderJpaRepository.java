package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.domain.model.OrderStatus;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);
}