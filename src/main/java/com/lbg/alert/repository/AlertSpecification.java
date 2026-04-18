package com.lbg.alert.repository;

import com.lbg.alert.entity.Alert;
import com.lbg.alert.enums.AlertStatus;
import com.lbg.alert.enums.AlertType;
import com.lbg.alert.enums.RiskBand;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

// repository/AlertSpecification.java
public class AlertSpecification {

    private AlertSpecification() {}

    public static Specification<Alert> withFilters(
            String status, String riskBand, String alertType,
            BigDecimal amountMin, BigDecimal amountMax) {

        return Specification
                .where(equalTo("status",    status    != null ? AlertStatus.valueOf(status)   : null))
                .and(equalTo("riskBand",    riskBand  != null ? RiskBand.valueOf(riskBand)    : null))
                .and(equalTo("alertType",   alertType != null ? AlertType.valueOf(alertType)  : null))
                .and(greaterThanOrEqual("amount", amountMin))
                .and(lessThanOrEqual("amount",    amountMax));
    }

    private static <T> Specification<Alert> equalTo(String field, T value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get(field), value);
    }

    private static Specification<Alert> greaterThanOrEqual(String field, BigDecimal value) {
        return (root, query, cb) ->
                value == null ? null : cb.greaterThanOrEqualTo(root.get(field), value);
    }

    private static Specification<Alert> lessThanOrEqual(String field, BigDecimal value) {
        return (root, query, cb) ->
                value == null ? null : cb.lessThanOrEqualTo(root.get(field), value);
    }
}