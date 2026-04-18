package com.lbg.alert.controller;

import com.lbg.alert.dto.request.AlertRequestDTO;
import com.lbg.alert.dto.request.AlertStatusUpdateDTO;
import com.lbg.alert.dto.response.AlertDetailDTO;
import com.lbg.alert.dto.response.AlertSummaryDTO;
import com.lbg.alert.dto.response.PagedResponseDTO;
import com.lbg.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

// controller/AlertController.java
@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Alerts", description = "Alert queue management APIs")
public class AlertController {

    private final AlertService alertService;

    // ── GET /api/alerts ──────────────────────────────────────
    @GetMapping
    @Operation(summary = "List alerts with optional filtering — paginated")
    public ResponseEntity<PagedResponseDTO<AlertSummaryDTO>> getAlerts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String riskBand,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) BigDecimal amountMin,
            @RequestParam(required = false) BigDecimal amountMax,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "20")  int size,
            @RequestParam(defaultValue = "triggeredAt") String sortBy,
            @RequestParam(defaultValue = "desc")        String sortDir) {

        log.info("GET /api/alerts status={} riskBand={} alertType={} page={} size={}",
                status, riskBand, alertType, page, size);
        return ResponseEntity.ok(
                alertService.getAlerts(status, riskBand, alertType,
                        amountMin, amountMax, page, size, sortBy, sortDir));
    }

    // ── GET /api/alerts/summary ──────────────────────────────
    // NOTE: mapped BEFORE /{id} to prevent Spring routing /summary as an ID
    @GetMapping("/summary")
    @Operation(summary = "Alert counts grouped by status (default), riskBand, or alertType")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Summary returned"),
        @ApiResponse(responseCode = "400", description = "Invalid groupBy value")
    })
    public ResponseEntity<Map<String, Long>> getSummary(
            @RequestParam(defaultValue = "status") String groupBy) {
        log.info("GET /api/alerts/summary groupBy={}", groupBy);
        return ResponseEntity.ok(alertService.getSummary(groupBy));
    }

    // ── GET /api/alerts/{id} ─────────────────────────────────
    @GetMapping("/{alertRef}")
    @Operation(summary = "Get full alert detail including flaggedRules")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert found"),
        @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    public ResponseEntity<AlertDetailDTO> getById(
            @PathVariable String alertRef) {
        log.info("GET /api/alerts/{}", alertRef);
        return ResponseEntity.ok(alertService.getAlertById(alertRef));
    }

    // ── POST /api/alerts ─────────────────────────────────────
    @PostMapping
    @Operation(summary = "Create a new alert")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alert created"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<AlertDetailDTO> createAlert(
            @Valid @RequestBody AlertRequestDTO requestDTO) {
        log.info("POST /api/alerts customerId={} alertType={}",
                requestDTO.getCustomerId(), requestDTO.getAlertType());
        AlertDetailDTO response = alertService.createAlert(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getAlertId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    // ── PATCH /api/alerts/{id}/status ────────────────────────
    @PatchMapping("/{alertRef}/status")
    @Operation(summary = "Update alert status — enforces valid transitions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated"),
        @ApiResponse(responseCode = "404", description = "Alert not found"),
        @ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<AlertDetailDTO> updateStatus(
            @PathVariable String alertRef,
            @Valid @RequestBody AlertStatusUpdateDTO dto) {
        log.info("PATCH /api/alerts/{}/status → {}", alertRef, dto.getStatus());
        return ResponseEntity.ok(alertService.updateStatus(alertRef, dto));
    }
}