package com.mk.www.smsmonitor.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.www.smsmonitor.application.service.SpendingCategoryService;
import com.mk.www.smsmonitor.domain.model.SpendingCategory;
import com.mk.www.smsmonitor.presentation.dto.SpendingCategoryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpendingCategoryController.class)
class SpendingCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpendingCategoryService spendingCategoryService;

    @Test
    @DisplayName("POST /api/spending-categories - 카테고리 생성 요청을 성공한다")
    void POST_api_spending_categories_카테고리_생성_요청을_성공한다() throws Exception {
        // given
        SpendingCategoryRequest request = new SpendingCategoryRequest();
        request.setName("식비");
        request.setStupidCostTarget(true);

        SpendingCategory createdCategory = SpendingCategory.builder()
                .id(1L)
                .name("식비")
                .isStupidCostTarget(true)
                .build();

        when(spendingCategoryService.createSpendingCategory(any(SpendingCategoryRequest.class))).thenReturn(createdCategory);

        // when & then
        mockMvc.perform(post("/api/spending-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/spending-categories/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("식비"));
    }

    @Test
    @DisplayName("GET /api/spending-categories - 모든 카테고리 조회 요청을 성공한다")
    void GET_api_spending_categories_모든_카테고리_조회_요청을_성공한다() throws Exception {
        // given
        List<SpendingCategory> categories = List.of(SpendingCategory.builder().id(1L).name("식비").build());
        when(spendingCategoryService.getAllSpendingCategories()).thenReturn(categories);

        // when & then
        mockMvc.perform(get("/api/spending-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("식비"));
    }

    @Test
    @DisplayName("DELETE /api/spending-categories/{id} - 카테고리 삭제 요청을 성공한다")
    void DELETE_api_spending_categories_id_카테고리_삭제_요청을_성공한다() throws Exception {
        // given
        when(spendingCategoryService.deleteSpendingCategory(1L)).thenReturn(true);

        // when & then
        mockMvc.perform(delete("/api/spending-categories/1"))
                .andExpect(status().isNoContent());
    }
}
