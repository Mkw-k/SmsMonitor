package com.mk.www.smsmonitor.presentation.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.www.smsmonitor.application.service.SmsService;
import com.mk.www.smsmonitor.application.service.TransactionService;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.presentation.dto.MemoRequest;
import com.mk.www.smsmonitor.presentation.dto.SmsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SmsService smsService;

    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("성공적인_SMS_처리에_대해_201CREATED_응답을_반환한다")
    void 성공적인_SMS_처리에_대해_201CREATED_응답을_반환한다() throws Exception {
        // given
        SmsRequest request = new SmsRequest();
        request.setSender("010-1234-5678");
        request.setMessage("KB국민카드 승인...");

        when(smsService.processNewSms(any(SmsRequest.class))).thenReturn(true);

        // when & then
        mockMvc.perform(post("/api/transactions/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET_api_transactions_거래내역_페이지_조회_요청을_성공한다")
    void GET_api_transactions_거래내역_페이지_조회_요청을_성공한다() throws Exception {
        // given
        Page<Transaction> page = new PageImpl<>(List.of(Transaction.builder().vendor("store1").build()));
        when(transactionService.getAllTransactions(any(Pageable.class))).thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/transactions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vendor").value("store1"));
    }

    @Test
    @DisplayName("GET_api_transactions_isStupid_true_멍청비용_페이지_조회_요청을_성공한다")
    void GET_api_transactions_isStupid_true_멍청비용_페이지_조회_요청을_성공한다() throws Exception {
        // given
        Page<Transaction> page = new PageImpl<>(List.of(Transaction.builder().vendor("stupid_store").isStupidCost(true).build()));
        when(transactionService.getStupidCostTransactions(any(Pageable.class))).thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/transactions")
                        .param("isStupid", "true")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vendor").value("stupid_store"))
                .andExpect(jsonPath("$.content[0].stupidCost").value(true));
    }

    @Test
    @DisplayName("PUT_api_transactions_id_memo_메모_수정_요청을_성공한다")
    void PUT_api_transactions_id_memo_메모_수정_요청을_성공한다() throws Exception {
        // given
        MemoRequest request = new MemoRequest();
        request.setMemo("새로운 메모");
        Transaction updatedTransaction = Transaction.builder().id(1L).memo("새로운 메모").build();

        when(transactionService.updateMemo(any(Long.class), any(MemoRequest.class))).thenReturn(Optional.of(updatedTransaction));

        // when & then
        mockMvc.perform(put("/api/transactions/1/memo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memo").value("새로운 메모"));
    }
}
