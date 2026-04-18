package com.lbg.alert.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class AlertDetailDTO {
    private String alertId;
    private String customerId;
    private String alertType;
    private String riskBand;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String assignedAnalyst;
    private Instant triggeredAt;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> flaggedRules;
}