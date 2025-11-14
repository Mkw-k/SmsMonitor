package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
