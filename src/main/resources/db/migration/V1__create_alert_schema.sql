-- ============================================================
-- Alert Service — H2 संस्करण
-- ============================================================

-- ─── ALERT ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS alert (
                                     id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     alert_ref           VARCHAR(20)     NOT NULL,
    customer_id         VARCHAR(30)     NOT NULL,
    alert_type          VARCHAR(30)     NOT NULL,
    risk_band           VARCHAR(10)     NOT NULL,
    amount              DECIMAL(19,4)   NOT NULL,
    currency_code       CHAR(3)         DEFAULT 'GBP' NOT NULL,
    status              VARCHAR(20)     DEFAULT 'NEW' NOT NULL,
    assigned_analyst    VARCHAR(100),
    triggered_at        TIMESTAMP       NOT NULL,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

-- Constraints
ALTER TABLE alert ADD CONSTRAINT uk_alert_ref UNIQUE (alert_ref);

-- Indexes
CREATE INDEX idx_alert_status        ON alert(status);
CREATE INDEX idx_alert_risk_band     ON alert(risk_band);
CREATE INDEX idx_alert_type          ON alert(alert_type);
CREATE INDEX idx_alert_customer      ON alert(customer_id);
CREATE INDEX idx_alert_triggered_at  ON alert(triggered_at);
CREATE INDEX idx_alert_analyst       ON alert(assigned_analyst);
CREATE INDEX idx_alert_status_risk   ON alert(status, risk_band, triggered_at);


-- ─── ALERT_FLAGGED_RULE ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS alert_flagged_rule (
                                                  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  alert_id    BIGINT          NOT NULL,
                                                  rule_code   VARCHAR(50)     NOT NULL,
    flagged_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT uk_alert_rule UNIQUE (alert_id, rule_code),
    CONSTRAINT fk_afr_alert FOREIGN KEY (alert_id)
    REFERENCES alert(id)
    ON DELETE CASCADE
    );

-- Indexes
CREATE INDEX idx_afr_alert       ON alert_flagged_rule(alert_id);
CREATE INDEX idx_afr_rule_code   ON alert_flagged_rule(rule_code);