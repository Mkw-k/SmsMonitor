package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.infrastructure.persistence.entity.SpendingCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpendingCategoryJpaRepository extends JpaRepository<SpendingCategoryEntity, Long> {
    Optional<SpendingCategoryEntity> findByName(String name);
}
