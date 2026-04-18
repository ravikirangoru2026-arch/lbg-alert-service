package com.lbg.alert.entity;

import com.lbg.alert.enums.AlertStatus;
import com.lbg.alert.enums.AlertType;
import com.lbg.alert.enums.RiskBand;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// entity/Alert.java
@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_ref", unique = true, nullable = false, length = 20)
    private String alertRef;

    @Column(name = "customer_id", nullable = false, length = 30)
    private String customerId;

    @Column(name = "alert_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    @Column(name = "risk_band", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(name = "assigned_analyst", length = 100)
    private String assignedAnalyst;

    @Column(name = "triggered_at", nullable = false)
    private Instant triggeredAt;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AlertFlaggedRule> flaggedRules = new ArrayList<>();
}