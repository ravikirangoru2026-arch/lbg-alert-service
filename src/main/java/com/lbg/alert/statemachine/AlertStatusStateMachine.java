package com.lbg.alert.statemachine;

import com.lbg.alert.enums.AlertStatus;
import com.lbg.alert.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class AlertStatusStateMachine {

    private static final Map<AlertStatus, Set<AlertStatus>> ALLOWED_TRANSITIONS =
        Map.of(
            AlertStatus.NEW,          Set.of(AlertStatus.UNDER_REVIEW),
            AlertStatus.UNDER_REVIEW, Set.of(AlertStatus.ESCALATED, AlertStatus.CLOSED),
            AlertStatus.ESCALATED,    Set.of(AlertStatus.CLOSED),
            AlertStatus.CLOSED,       Set.of()
        );

    public void validate(AlertStatus current, AlertStatus requested) {
        Set<AlertStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowed.contains(requested)) {
            throw new BusinessRuleException(
                String.format("Invalid status transition from %s to %s", current, requested)
            );
        }
    }
}