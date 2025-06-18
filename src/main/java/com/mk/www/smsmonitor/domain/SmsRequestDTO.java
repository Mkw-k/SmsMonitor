package com.mk.www.smsmonitor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class SmsRequestDTO {
    private String message;
    private String sender;
}
