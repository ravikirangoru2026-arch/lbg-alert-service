-- ============================================================
-- Alert Service  —  V2__seed_alert_data.sql
-- Seed: 20 alert records from alerts.json
-- ============================================================

-- ─── ALERTS ─────────────────────────────────────────────────
INSERT INTO alert (alert_ref, customer_id, alert_type, risk_band, amount, currency_code, status, assigned_analyst, triggered_at) VALUES
('ALT-00001', 'CUST-1042', 'STRUCTURING',       'HIGH',   9800.0000,   'GBP', 'NEW',          NULL,         '2024-01-15 09:12:00'),
('ALT-00002', 'CUST-2187', 'APP_SCAM',           'HIGH',   24500.0000,  'GBP', 'UNDER_REVIEW', 's.okafor',   '2024-01-18 14:33:00'),
('ALT-00003', 'CUST-3301', 'UNUSUAL_ACTIVITY',   'MEDIUM', 4200.0000,   'GBP', 'NEW',          NULL,         '2024-01-22 11:05:00'),
('ALT-00004', 'CUST-4455', 'SANCTIONS_HIT',      'HIGH',   78000.0000,  'GBP', 'ESCALATED',    't.bergmann', '2024-01-25 08:47:00'),
('ALT-00005', 'CUST-5509', 'APP_SCAM',           'MEDIUM', 3750.0000,   'GBP', 'NEW',          NULL,         '2024-01-28 16:22:00'),
('ALT-00006', 'CUST-6612', 'ACCOUNT_TAKEOVER',   'HIGH',   15200.0000,  'GBP', 'UNDER_REVIEW', 'j.rahman',   '2024-02-01 07:58:00'),
('ALT-00007', 'CUST-7734', 'UNUSUAL_ACTIVITY',   'LOW',    1100.0000,   'GBP', 'CLOSED',       'p.nwosu',    '2024-02-03 13:14:00'),
('ALT-00008', 'CUST-2187', 'APP_SCAM',           'HIGH',   31000.0000,  'GBP', 'UNDER_REVIEW', 's.okafor',   '2024-02-05 10:30:00'),
('ALT-00009', 'CUST-8821', 'STRUCTURING',        'MEDIUM', 7400.0000,   'GBP', 'NEW',          NULL,         '2024-02-08 09:01:00'),
('ALT-00010', 'CUST-9043', 'SANCTIONS_HIT',      'HIGH',   145000.0000, 'GBP', 'ESCALATED',    't.bergmann', '2024-02-10 15:44:00'),
('ALT-00011', 'CUST-1120', 'UNUSUAL_ACTIVITY',   'LOW',    850.0000,    'GBP', 'CLOSED',       'p.nwosu',    '2024-02-12 12:00:00'),
('ALT-00012', 'CUST-3301', 'ACCOUNT_TAKEOVER',   'MEDIUM', 6800.0000,   'GBP', 'NEW',          NULL,         '2024-02-14 08:25:00'),
('ALT-00013', 'CUST-4455', 'SANCTIONS_HIT',      'MEDIUM', 12000.0000,  'GBP', 'UNDER_REVIEW', 't.bergmann', '2024-02-16 10:55:00'),
('ALT-00014', 'CUST-1234', 'APP_SCAM',           'MEDIUM', 5100.0000,   'GBP', 'CLOSED',       's.okafor',   '2024-02-19 14:40:00'),
('ALT-00015', 'CUST-5678', 'STRUCTURING',        'LOW',    1950.0000,   'GBP', 'CLOSED',       'p.nwosu',    '2024-02-21 09:30:00'),
('ALT-00016', 'CUST-6612', 'UNUSUAL_ACTIVITY',   'MEDIUM', 3300.0000,   'GBP', 'NEW',          NULL,         '2024-02-24 11:18:00'),
('ALT-00017', 'CUST-7890', 'APP_SCAM',           'HIGH',   47500.0000,  'GBP', 'ESCALATED',    's.okafor',   '2024-02-27 08:05:00'),
('ALT-00018', 'CUST-2345', 'STRUCTURING',        'MEDIUM', 8750.0000,   'GBP', 'NEW',          NULL,         '2024-03-01 10:20:00'),
('ALT-00019', 'CUST-9876', 'ACCOUNT_TAKEOVER',   'LOW',    900.0000,    'GBP', 'CLOSED',       'p.nwosu',    '2024-03-04 13:45:00'),
('ALT-00020', 'CUST-1042', 'STRUCTURING',        'HIGH',   9600.0000,   'GBP', 'NEW',          NULL,         '2024-03-07 09:55:00');

-- ─── FLAGGED RULES ───────────────────────────────────────────
-- ALT-00001
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CASH_THRESHOLD'      FROM alert WHERE alert_ref = 'ALT-00001';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'MULTI_BRANCH'        FROM alert WHERE alert_ref = 'ALT-00001';
-- ALT-00002
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'RAPID_DISPERSAL'     FROM alert WHERE alert_ref = 'ALT-00002';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'NEW_ACCOUNT'         FROM alert WHERE alert_ref = 'ALT-00002';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VICTIM_REPORT'       FROM alert WHERE alert_ref = 'ALT-00002';
-- ALT-00003
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VELOCITY_BREACH'     FROM alert WHERE alert_ref = 'ALT-00003';
-- ALT-00004
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'OFAC_MATCH'          FROM alert WHERE alert_ref = 'ALT-00004';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'HMT_MATCH'           FROM alert WHERE alert_ref = 'ALT-00004';
-- ALT-00005
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'RAPID_DISPERSAL'     FROM alert WHERE alert_ref = 'ALT-00005';
-- ALT-00006
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'NEW_DEVICE'          FROM alert WHERE alert_ref = 'ALT-00006';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'UNUSUAL_LOCATION'    FROM alert WHERE alert_ref = 'ALT-00006';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'LARGE_TRANSFER'      FROM alert WHERE alert_ref = 'ALT-00006';
-- ALT-00007
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VELOCITY_BREACH'     FROM alert WHERE alert_ref = 'ALT-00007';
-- ALT-00008
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'RAPID_DISPERSAL'     FROM alert WHERE alert_ref = 'ALT-00008';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CRYPTO_TRANSFER'     FROM alert WHERE alert_ref = 'ALT-00008';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VICTIM_REPORT'       FROM alert WHERE alert_ref = 'ALT-00008';
-- ALT-00009
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CASH_THRESHOLD'      FROM alert WHERE alert_ref = 'ALT-00009';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'MULTI_BRANCH'        FROM alert WHERE alert_ref = 'ALT-00009';
-- ALT-00010
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'OFAC_MATCH'          FROM alert WHERE alert_ref = 'ALT-00010';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'HIGH_RISK_JURISDICTION' FROM alert WHERE alert_ref = 'ALT-00010';
-- ALT-00011
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VELOCITY_BREACH'     FROM alert WHERE alert_ref = 'ALT-00011';
-- ALT-00012
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'NEW_DEVICE'          FROM alert WHERE alert_ref = 'ALT-00012';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'UNUSUAL_LOCATION'    FROM alert WHERE alert_ref = 'ALT-00012';
-- ALT-00013
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'HMT_MATCH'           FROM alert WHERE alert_ref = 'ALT-00013';
-- ALT-00014
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'RAPID_DISPERSAL'     FROM alert WHERE alert_ref = 'ALT-00014';
-- ALT-00015
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CASH_THRESHOLD'      FROM alert WHERE alert_ref = 'ALT-00015';
-- ALT-00016
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VELOCITY_BREACH'     FROM alert WHERE alert_ref = 'ALT-00016';
-- ALT-00017
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'RAPID_DISPERSAL'     FROM alert WHERE alert_ref = 'ALT-00017';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'VICTIM_REPORT'       FROM alert WHERE alert_ref = 'ALT-00017';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CRYPTO_TRANSFER'     FROM alert WHERE alert_ref = 'ALT-00017';
-- ALT-00018
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CASH_THRESHOLD'      FROM alert WHERE alert_ref = 'ALT-00018';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'MULTI_BRANCH'        FROM alert WHERE alert_ref = 'ALT-00018';
-- ALT-00019
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'NEW_DEVICE'          FROM alert WHERE alert_ref = 'ALT-00019';
-- ALT-00020
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'CASH_THRESHOLD'      FROM alert WHERE alert_ref = 'ALT-00020';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'MULTI_BRANCH'        FROM alert WHERE alert_ref = 'ALT-00020';
INSERT INTO alert_flagged_rule (alert_id, rule_code) SELECT id, 'REPEAT_OFFENDER'     FROM alert WHERE alert_ref = 'ALT-00020';
