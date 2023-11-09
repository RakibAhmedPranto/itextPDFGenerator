package com.example.testPDF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "BC_HALF_YRLY_STATEMENT")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HalfYearlyAccBalanceStatement extends Statement {
    private static final long serialVersionUID = 1L;

    public String accountOpeningDate;

    public String customerCommunicationAddress;

    public String customerPermanentAddress;

    public String customerWorkAddress;

    public String accountCurrencyCode;

    public BigDecimal balanceAsOnDate;

    public String balanceIndicator;

    public String solName;

    @Column(name = "BRNCH_CODE")
    public String branchCode;

    public String schemeType;
    @Column(nullable = true)
    public LocalDate balanceConfirmationDate;

    @Column(name = "STATUS", length = 30)
    @Enumerated(EnumType.STRING)
    public StatementStatus status;
}