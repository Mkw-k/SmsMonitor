package com.mk.www.smsmonitor.presentation;

import com.mk.www.smsmonitor.application.SmsReceiveApplicationService;
import com.mk.www.smsmonitor.domain.SmsRequestDTO;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Slf4j
public class SmsReceiverController {

    private final SmsReceiveApplicationService smsProcessingService;

    @PostMapping
    public ResponseEntity<?> receiveSms(@RequestBody SmsRequestDTO smsRequestDTO) {
        OrderJpaEntity order = smsProcessingService.handleSms(smsRequestDTO.getMessage(), smsRequestDTO.getSender());
        return ResponseEntity.ok().body(order);
    }

    @GetMapping
    public ResponseEntity<?> getSms(@RequestParam(value = "msg", required = false) String msg) {
      log.info("에크 {}", msg);
      return ResponseEntity.ok().body("에크 " + msg);
    }
}