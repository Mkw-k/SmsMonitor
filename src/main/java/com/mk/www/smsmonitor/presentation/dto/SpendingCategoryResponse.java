package com.mk.www.smsmonitor.presentation.dto;

import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SpendingCategoryResponse {
    private Long id;
    private String name;
    private boolean isStupidCostTarget;

    public static SpendingCategoryResponse from(SpendingCategory spendingCategory) {
        return SpendingCategoryResponse.builder()
                .id(spendingCategory.getId())
                .name(spendingCategory.getName())
                .isStupidCostTarget(spendingCategory.isStupidCostTarget())
                .build();
    }
}
