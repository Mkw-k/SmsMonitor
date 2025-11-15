package com.mk.www.smsmonitor.infrastructure.persistence.mapper;

import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.SpendingCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class SpendingCategoryMapper {

    public SpendingCategory toDomain(SpendingCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return SpendingCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isStupidCostTarget(entity.isStupidCostTarget())
                .build();
    }

    public SpendingCategoryEntity toEntity(SpendingCategory domain) {
        if (domain == null) {
            return null;
        }
        SpendingCategoryEntity entity = new SpendingCategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setStupidCostTarget(domain.isStupidCostTarget());
        return entity;
    }
}
