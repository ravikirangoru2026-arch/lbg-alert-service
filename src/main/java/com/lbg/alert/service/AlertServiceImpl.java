package com.lbg.alert.service;

import com.lbg.alert.dto.request.AlertRequestDTO;
import com.lbg.alert.dto.request.AlertStatusUpdateDTO;
import com.lbg.alert.dto.response.AlertDetailDTO;
import com.lbg.alert.dto.response.AlertSummaryDTO;
import com.lbg.alert.dto.response.PagedResponseDTO;
import com.lbg.alert.entity.Alert;
import com.lbg.alert.enums.AlertStatus;
import com.lbg.alert.enums.AlertType;
import com.lbg.alert.enums.RiskBand;
import com.lbg.alert.exception.ResourceNotFoundException;
import com.lbg.alert.mapper.AlertMapper;
import com.lbg.alert.repository.AlertRepository;
import com.lbg.alert.repository.AlertSpecification;
import com.lbg.alert.statemachine.AlertStatusStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AlertServiceImpl implements AlertService {

    private static final Set<String> VALID_SORT_FIELDS =
            Set.of("triggeredAt", "amount", "status", "riskBand");
    private static final Set<String> VALID_GROUP_BY =
            Set.of("status", "riskBand", "alertType");

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AlertStatusStateMachine stateMachine;

    // ── GET /api/alerts ──────────────────────────────────────
    @Override
    public PagedResponseDTO<AlertSummaryDTO> getAlerts(
            String status, String riskBand, String alertType,
            BigDecimal amountMin, BigDecimal amountMax,
            int page, int size, String sortBy, String sortDir) {

        // Validate enum params early — throw 400 not 500
        validateEnumParam(status,    AlertStatus.class, "status");
        validateEnumParam(riskBand,  RiskBand.class,    "riskBand");
        validateEnumParam(alertType, AlertType.class,   "alertType");

        String resolvedSort = VALID_SORT_FIELDS.contains(sortBy) ? sortBy : "triggeredAt";
        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(resolvedSort).ascending()
                : Sort.by(resolvedSort).descending();

        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);
        Specification<Alert> spec = AlertSpecification.withFilters(
                status, riskBand, alertType, amountMin, amountMax);

        Page<AlertSummaryDTO> result = alertRepository.findAll(spec, pageable)
                .map(alertMapper::toSummaryDTO);

        log.debug("Fetched {} alerts (page={}, size={})", result.getTotalElements(), page, size);
        return PagedResponseDTO.from(result);
    }

    // ── GET /api/alerts/{id} ─────────────────────────────────
    @Override
    public AlertDetailDTO getAlertById(String alertRef) {
        log.debug("Fetching alert detail for ref={}", alertRef);
        Alert alert = findByRefOrThrow(alertRef);
        return alertMapper.toDetailDTO(alert);
    }

    // ── POST /api/alerts ─────────────────────────────────────
    @Override
    @Transactional
    public AlertDetailDTO createAlert(AlertRequestDTO requestDTO) {
        log.info("Creating alert for customerId={}, type={}", 
                requestDTO.getCustomerId(), requestDTO.getAlertType());
        Alert alert = alertMapper.toEntity(requestDTO);
        Alert saved  = alertRepository.save(alert);
        log.info("Alert created: ref={}, id={}", saved.getAlertRef(), saved.getId());
        return alertMapper.toDetailDTO(saved);
    }

    // ── PATCH /api/alerts/{id}/status ────────────────────────
    @Override
    @Transactional
    public AlertDetailDTO updateStatus(String alertRef, AlertStatusUpdateDTO dto) {
        Alert alert = findByRefOrThrow(alertRef);
        AlertStatus requested = AlertStatus.valueOf(dto.getStatus());

        log.info("Status transition requested: ref={} | {} → {}",
                alertRef, alert.getStatus(), requested);

        stateMachine.validate(alert.getStatus(), requested);
        alert.setStatus(requested);
        Alert updated = alertRepository.save(alert);

        log.info("Status updated: ref={} → {}", alertRef, updated.getStatus());
        return alertMapper.toDetailDTO(updated);
    }

    // ── GET /api/alerts/summary ──────────────────────────────
    @Override
    public Map<String, Long> getSummary(String groupBy) {
        String resolved = (groupBy == null || groupBy.isBlank()) ? "status" : groupBy;
        if (!VALID_GROUP_BY.contains(resolved)) {
            throw new IllegalArgumentException(
                    "groupBy must be one of: status, riskBand, alertType");
        }
        log.debug("Fetching alert summary grouped by={}", resolved);

        List<AlertRepository.SummaryProjection> rows = switch (resolved) {
            case "riskBand"   -> alertRepository.countByRiskBand();
            case "alertType"  -> alertRepository.countByAlertType();
            default           -> alertRepository.countByStatus();
        };

        return rows.stream().collect(Collectors.toMap(
                p -> p.getGroupKey().toString(),
                AlertRepository.SummaryProjection::getCount,
                (a, b) -> a,
                LinkedHashMap::new
        ));
    }

    // ── Helpers ──────────────────────────────────────────────
    private Alert findByRefOrThrow(String alertRef) {
        return alertRepository.findByAlertRef(alertRef)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alert not found with ref: " + alertRef));
    }

    private <E extends Enum<E>> void validateEnumParam(
            String value, Class<E> enumClass, String paramName) {
        if (value == null) return;
        try { Enum.valueOf(enumClass, value); }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value '" + value + "' for parameter '" + paramName + "'");
        }
    }
}