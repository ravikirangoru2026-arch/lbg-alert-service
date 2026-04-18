package com.lbg.alert.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRequestDTO {

    @NotBlank(message = "Customer ID must not be blank")
    private String customerId;

    @NotBlank(message = "Alert type must not be blank")
    @Pattern(
        regexp = "STRUCTURING|APP_SCAM|SANCTIONS_HIT|UNUSUAL_ACTIVITY|ACCOUNT_TAKEOVER",
        message = "Must be one of: STRUCTURING, APP_SCAM, SANCTIONS_HIT, UNUSUAL_ACTIVITY, ACCOUNT_TAKEOVER"
    )
    private String alertType;

    @NotNull(message = "Risk band must not be null")
    @Pattern(
        regexp = "HIGH|MEDIUM|LOW",
        message = "Risk band must be HIGH, MEDIUM, or LOW"
    )
    private String riskBand;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Currency must not be blank")
    @Size(min = 3, max = 3, message = "Currency must be a 3-character ISO code")
    private String currency;

    @NotNull(message = "Triggered date must not be null")
    @PastOrPresent(message = "Triggered date must not be in the future")
    private Instant triggeredAt;

    @NotEmpty(message = "At least one flagged rule must be provided")
    private List<String> flaggedRules;

    private String assignedAnalyst;
}