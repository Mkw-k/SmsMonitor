package com.mk.www.smsmonitor.presentation;

import com.mk.www.smsmonitor.application.GoogleSheetService;
import com.mk.www.smsmonitor.domain.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final com.mk.www.smsmonitor.application.GoogleSheetService sheetService;

    public PaymentController(GoogleSheetService sheetService) {
        this.sheetService = sheetService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest request) {
        try {
            boolean success = sheetService.updateStatusIfMatch(request.get번호(), request.get가격());
            if (success) {
                return ResponseEntity.ok("입금완료 처리됨");
            } else {
                return ResponseEntity.badRequest().body("일치하는 내역 없음");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }
}
