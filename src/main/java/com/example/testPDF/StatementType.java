package com.example.testPDF;

public enum StatementType {
    HALF_YEARLY_ACCOUNT_BALANCE_STATEMENT("half-yearly-acc-blnc-statement"),
    HALF_YEARLY_LOAN_BALANCE_STATEMENT("half-yearly-loan-blnc-statement");

    private String statementLocation;

    private StatementType(String statementLocation) {
        this.statementLocation = statementLocation;
    }

    public String getStatementLocation() {
        return this.statementLocation;
    }
}
