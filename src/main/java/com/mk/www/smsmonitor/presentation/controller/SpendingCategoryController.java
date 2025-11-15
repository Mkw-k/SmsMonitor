package com.mk.www.smsmonitor.presentation.controller;

import com.mk.www.smsmonitor.application.service.SpendingCategoryService;
import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import com.mk.www.smsmonitor.presentation.dto.SpendingCategoryRequest;
import com.mk.www.smsmonitor.presentation.dto.SpendingCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "SpendingCategory", description = "소비 카테고리 관리 API")
@RestController
@RequestMapping("/api/spending-categories")
@RequiredArgsConstructor
public class SpendingCategoryController {

    private final SpendingCategoryService spendingCategoryService;

    @Operation(summary = "소비 카테고리 생성", description = "새로운 카테고리 생성")
    @PostMapping
    public ResponseEntity<SpendingCategoryResponse> createSpendingCategory(@RequestBody SpendingCategoryRequest request) {
        SpendingCategory spendingCategory = spendingCategoryService.createSpendingCategory(request);
        SpendingCategoryResponse response = SpendingCategoryResponse.from(spendingCategory);
        return ResponseEntity.created(URI.create("/api/spending-categories/" + response.getId())).body(response);
    }

    @Operation(summary = "모든 소비 카테고리 조회", description = "모든 카테고리 목록 조회")
    @GetMapping
    public ResponseEntity<List<SpendingCategoryResponse>> getAllSpendingCategories() {
        List<SpendingCategoryResponse> responses = spendingCategoryService.getAllSpendingCategories().stream()
                .map(SpendingCategoryResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "소비 카테고리 수정", description = "특정 카테고리의 정보를 수정")
    @PutMapping("/{id}")
    public ResponseEntity<SpendingCategoryResponse> updateSpendingCategory(@PathVariable Long id, @RequestBody SpendingCategoryRequest request) {
        return spendingCategoryService.updateSpendingCategory(id, request)
                .map(SpendingCategoryResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "소비 카테고리 삭제", description = "특정 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpendingCategory(@PathVariable Long id) {
        boolean deleted = spendingCategoryService.deleteSpendingCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
