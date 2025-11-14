package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
}
