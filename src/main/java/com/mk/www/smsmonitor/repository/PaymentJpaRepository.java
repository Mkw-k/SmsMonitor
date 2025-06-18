package com.mk.www.smsmonitor.repository;

import com.mk.www.smsmonitor.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
