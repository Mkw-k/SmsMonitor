package com.mk.www.smsmonitor.presentation.controller;

import com.mk.www.smsmonitor.application.service.SmsService;
import com.mk.www.smsmonitor.application.service.TransactionService;
import com.mk.www.smsmonitor.presentation.dto.MemoRequest;
import com.mk.www.smsmonitor.presentation.dto.ResultDTO;
import com.mk.www.smsmonitor.presentation.dto.SmsRequest;
import com.mk.www.smsmonitor.presentation.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transaction", description = "거래내역 관리 API")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final SmsService smsService;
    private final TransactionService transactionService;
    private static final String TID_KEY = "tid";

    @Operation(summary = "SMS 수신", description = "수신된 SMS 메시지를 파싱하여 거래내역으로 저장")
    @PostMapping("/sms")
    public ResponseEntity<ResultDTO<Void>> receiveSms(@RequestBody SmsRequest request) {
        String tid = MDC.get(TID_KEY);
        boolean success = smsService.processNewSms(request);

        if (success) {
            return ResponseEntity.ok(ResultDTO.success(null, tid));
        } else {
            return ResponseEntity.badRequest().body(ResultDTO.error("SMS_PARSE_ERROR", "SMS 파싱에 실패했습니다.", tid));
        }
    }

    @Operation(summary = "거래내역 조회", description = "모든 거래내역 또는 멍청비용 내역을 페이지 단위로 조회")
    @GetMapping
    public ResponseEntity<ResultDTO<Page<TransactionResponse>>> getAllTransactions(
            @RequestParam(name = "isStupid", required = false) Boolean isStupid,
            Pageable pageable) {

        String tid = MDC.get(TID_KEY);
        Page<TransactionResponse> responses;
        if (isStupid != null && isStupid) {
            responses = transactionService.getStupidCostTransactions(pageable)
                    .map(TransactionResponse::from);
        } else {
            responses = transactionService.getAllTransactions(pageable)
                    .map(TransactionResponse::from);
        }
        return ResponseEntity.ok(ResultDTO.success(responses, tid));
    }

    @Operation(summary = "메모 수정", description = "특정 거래내역 메모 추가 또는 수정")
    @PutMapping("/{id}/memo")
    public ResponseEntity<ResultDTO<TransactionResponse>> updateMemo(@PathVariable Long id, @RequestBody MemoRequest request) {
        String tid = MDC.get(TID_KEY);
        return transactionService.updateMemo(id, request)
                .map(TransactionResponse::from)
                .map(data -> ResponseEntity.ok(ResultDTO.success(data, tid)))
                .orElse(ResponseEntity.status(404).body(ResultDTO.error("NOT_FOUND", "해당 거래내역을 찾을 수 없습니다.", tid)));
    }
}
