package com.lbg.alert.mapper;

import com.lbg.alert.dto.request.AlertRequestDTO;
import com.lbg.alert.dto.response.AlertDetailDTO;
import com.lbg.alert.dto.response.AlertSummaryDTO;
import com.lbg.alert.entity.Alert;
import com.lbg.alert.entity.AlertFlaggedRule;
import com.lbg.alert.enums.AlertStatus;
import com.lbg.alert.enums.AlertType;
import com.lbg.alert.enums.RiskBand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertMapper {

    public AlertSummaryDTO toSummaryDTO(Alert alert) {
        return AlertSummaryDTO.builder()
                .alertId(alert.getAlertRef())
                .customerId(alert.getCustomerId())
                .alertType(alert.getAlertType().name())
                .riskBand(alert.getRiskBand().name())
                .amount(alert.getAmount())
                .triggeredAt(alert.getTriggeredAt())
                .status(alert.getStatus().name())
                .build();
    }

    public AlertDetailDTO toDetailDTO(Alert alert) {
        List<String> rules = alert.getFlaggedRules().stream()
                .map(AlertFlaggedRule::getRuleCode)
                .toList();
        return AlertDetailDTO.builder()
                .alertId(alert.getAlertRef())
                .customerId(alert.getCustomerId())
                .alertType(alert.getAlertType().name())
                .riskBand(alert.getRiskBand().name())
                .amount(alert.getAmount())
                .currency(alert.getCurrencyCode())
                .status(alert.getStatus().name())
                .assignedAnalyst(alert.getAssignedAnalyst())
                .triggeredAt(alert.getTriggeredAt())
                .createdAt(alert.getCreatedAt())
                .updatedAt(alert.getUpdatedAt())
                .flaggedRules(rules)
                .build();
    }

    public Alert toEntity(AlertRequestDTO dto) {
        Alert alert = Alert.builder()
                .alertRef(generateRef())
                .customerId(dto.getCustomerId())
                .alertType(AlertType.valueOf(dto.getAlertType()))
                .riskBand(RiskBand.valueOf(dto.getRiskBand()))
                .amount(dto.getAmount())
                .currencyCode(dto.getCurrency())
                .status(AlertStatus.NEW)
                .assignedAnalyst(dto.getAssignedAnalyst())
                .triggeredAt(dto.getTriggeredAt())
                .build();

        if (dto.getFlaggedRules() != null) {
            dto.getFlaggedRules().forEach(code -> {
                AlertFlaggedRule rule = AlertFlaggedRule.builder()
                        .alert(alert)
                        .ruleCode(code)
                        .build();
                alert.getFlaggedRules().add(rule);
            });
        }
        return alert;
    }

    private String generateRef() {
        return "ALT-" + String.format("%05d", System.currentTimeMillis() % 100000);
    }
}