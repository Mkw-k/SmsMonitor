package com.mk.www.smsmonitor.infrastructure.persistence.repository;

import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import com.mk.www.smsmonitor.domain.repository.SpendingCategoryRepository;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.SpendingCategoryEntity;
import com.mk.www.smsmonitor.infrastructure.persistence.mapper.SpendingCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpendingCategoryRepositoryImpl implements SpendingCategoryRepository {

    private final SpendingCategoryJpaRepository spendingCategoryJpaRepository;
    private final SpendingCategoryMapper spendingCategoryMapper;

    @Override
    public Optional<SpendingCategory> findByName(String name) {
        return spendingCategoryJpaRepository.findByName(name)
                .map(spendingCategoryMapper::toDomain);
    }

    @Override
    public SpendingCategory save(SpendingCategory spendingCategory) {
        SpendingCategoryEntity entity = spendingCategoryMapper.toEntity(spendingCategory);
        SpendingCategoryEntity savedEntity = spendingCategoryJpaRepository.save(entity);
        return spendingCategoryMapper.toDomain(savedEntity);
    }

    @Override
    public List<SpendingCategory> findAll() {
        return spendingCategoryJpaRepository.findAll().stream()
                .map(spendingCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SpendingCategory> findById(Long id) {
        return spendingCategoryJpaRepository.findById(id)
                .map(spendingCategoryMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        spendingCategoryJpaRepository.deleteById(id);
    }
}
