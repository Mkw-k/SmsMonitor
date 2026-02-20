package com.mk.www.smsmonitor.presentation.controller;

import com.mk.www.smsmonitor.application.service.SpendingCategoryService;
import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import com.mk.www.smsmonitor.presentation.dto.ResultDTO;
import com.mk.www.smsmonitor.presentation.dto.SpendingCategoryRequest;
import com.mk.www.smsmonitor.presentation.dto.SpendingCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SpendingCategory", description = "소비 카테고리 관리 API")
@RestController
@RequestMapping("/api/spending-categories")
@RequiredArgsConstructor
public class SpendingCategoryController {

    private final SpendingCategoryService spendingCategoryService;
    private static final String TID_KEY = "tid";

    @Operation(summary = "소비 카테고리 생성", description = "새로운 카테고리 생성")
    @PostMapping
    public ResponseEntity<ResultDTO<SpendingCategoryResponse>> createSpendingCategory(@RequestBody SpendingCategoryRequest request) {
        String tid = MDC.get(TID_KEY);
        SpendingCategory spendingCategory = spendingCategoryService.createSpendingCategory(request);
        SpendingCategoryResponse response = SpendingCategoryResponse.from(spendingCategory);
        return ResponseEntity.ok(ResultDTO.success(response, tid));
    }

    @Operation(summary = "모든 소비 카테고리 조회", description = "모든 카테고리 목록 조회")
    @GetMapping
    public ResponseEntity<ResultDTO<List<SpendingCategoryResponse>>> getAllSpendingCategories() {
        String tid = MDC.get(TID_KEY);
        List<SpendingCategoryResponse> responses = spendingCategoryService.getAllSpendingCategories().stream()
                .map(SpendingCategoryResponse::from)
                .toList();
        return ResponseEntity.ok(ResultDTO.success(responses, tid));
    }

    @Operation(summary = "소비 카테고리 수정", description = "특정 카테고리의 정보를 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO<SpendingCategoryResponse>> updateSpendingCategory(@PathVariable Long id, @RequestBody SpendingCategoryRequest request) {
        String tid = MDC.get(TID_KEY);
        return spendingCategoryService.updateSpendingCategory(id, request)
                .map(SpendingCategoryResponse::from)
                .map(data -> ResponseEntity.ok(ResultDTO.success(data, tid)))
                .orElse(ResponseEntity.status(404).body(ResultDTO.error("NOT_FOUND", "해당 카테고리를 찾을 수 없습니다.", tid)));
    }

    @Operation(summary = "소비 카테고리 삭제", description = "특정 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultDTO<Void>> deleteSpendingCategory(@PathVariable Long id) {
        String tid = MDC.get(TID_KEY);
        boolean deleted = spendingCategoryService.deleteSpendingCategory(id);
        if (deleted) {
            return ResponseEntity.ok(ResultDTO.success(null, tid));
        } else {
            return ResponseEntity.status(404).body(ResultDTO.error("NOT_FOUND", "해당 카테고리를 찾을 수 없습니다.", tid));
        }
    }
}
