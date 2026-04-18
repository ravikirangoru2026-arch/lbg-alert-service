package com.lbg.alert.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class AlertSummaryDTO {
    private String alertId;        // alertRef e.g. ALT-00001
    private String customerId;
    private String alertType;
    private String riskBand;
    private BigDecimal amount;
    private Instant triggeredAt;
    private String status;
}