package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
