package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.domain.OrderStatus;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    List<OrderJpaEntity> findByStatus(OrderStatus status);
}