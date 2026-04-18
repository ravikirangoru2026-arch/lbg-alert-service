package com.lbg.alert.service;

import com.lbg.alert.dto.request.AlertRequestDTO;
import com.lbg.alert.dto.request.AlertStatusUpdateDTO;
import com.lbg.alert.dto.response.AlertDetailDTO;
import com.lbg.alert.dto.response.AlertSummaryDTO;
import com.lbg.alert.dto.response.PagedResponseDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface AlertService {
    PagedResponseDTO<AlertSummaryDTO> getAlerts(String status, String riskBand,
                                                String alertType, BigDecimal amountMin, BigDecimal amountMax,
                                                int page, int size, String sortBy, String sortDir);
    AlertDetailDTO getAlertById(String alertRef);
    AlertDetailDTO createAlert(AlertRequestDTO requestDTO);
    AlertDetailDTO updateStatus(String alertRef, AlertStatusUpdateDTO dto);
    Map<String, Long> getSummary(String groupBy);
}