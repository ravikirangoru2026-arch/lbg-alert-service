package com.lbg.alert.enums;

public enum AlertType {
    STRUCTURING, APP_SCAM, SANCTIONS_HIT, UNUSUAL_ACTIVITY, ACCOUNT_TAKEOVER;

    public static boolean isValid(String value) {
        try { valueOf(value); return true; }
        catch (IllegalArgumentException e) { return false; }
    }
}
