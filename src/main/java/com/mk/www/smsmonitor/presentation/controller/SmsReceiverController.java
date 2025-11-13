package com.mk.www.smsmonitor.presentation.controller;

import com.mk.www.smsmonitor.application.dto.SmsRequestDTO;
import com.mk.www.smsmonitor.application.service.SmsReceiveApplicationService;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;
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
        OrderEntity order = smsProcessingService.handleSms(smsRequestDTO.getMessage(), smsRequestDTO.getSender());
        return ResponseEntity.ok().body(order);
    }

    @GetMapping
    public ResponseEntity<?> getSms(@RequestParam(value = "msg", required = false) String msg) {
      log.info("에크 {}", msg);
      return ResponseEntity.ok().body("에크 " + msg);
    }
}