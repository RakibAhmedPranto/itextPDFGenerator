package com.example.testPDF;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileLocationResolver {
    @Value("${pdf.dump.location}")
    private String pdfDumpLocation;

    @Value("${temporary.generate.location}")
    public String tempGenLocation;

    @Value("${archive.destination.path}")
    public String archivePath;

    @Value("${fgen.archive.path}")
    private String fgenArchivePath;

    @Value("${migrated.archive.path}")
    public String migratedArchivePath;
    public String tempDirectoryPath(Statement statement) {
        if (statement instanceof HalfYearlyAccBalanceStatement) {
            return tempGenLocation + statement.statementType.getStatementLocation() + File.separator
                    + getCurrentTimeString() + "-" + MaskingUtil.xMaskAccountNo(statement.accountNumber) + ".pdf";
        }
        return null;
    }

    public static String getCurrentTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS"));
    }
}
