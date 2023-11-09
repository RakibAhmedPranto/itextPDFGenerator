package com.example.testPDF;

public enum StatementStatus {
    INSERTED("1"),
    MAIL_SENT("2"),
    MAIL_NOT_SENT("3"),
    FAILED("4");

    private String statusIndex;

    private StatementStatus(String statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getStatusIndex() {
        return this.statusIndex;
    }
}
