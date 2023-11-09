package com.example.testPDF;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class Statement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long taskId;

    public String accountNumber;

    public String solId;

    public String schemeCode;

    @Enumerated(EnumType.STRING)
    public StatementType statementType;

    public String cifId;

    public String customerTitle;

    public String phoneNumber;

    public String email;

    public LocalDateTime insertedAt;

    @Column(nullable = true)
    public LocalDateTime emailSendAt;

    @Transient
    public Integer serialNumber;

    public String year;

    public String month;

    @Column(nullable = true)
    public Boolean isHardcopyRequired = false;
}
