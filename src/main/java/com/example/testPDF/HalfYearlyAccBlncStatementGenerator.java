package com.example.testPDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class HalfYearlyAccBlncStatementGenerator {

    private BaseFont boldBaseFont;
    private BaseFont baseFont;
    private Font font;
    private Font boldFont;
    private Font fontSmall;
    private Font boldFontSmall;
    private Font boldFontUnderLiner;
    private HalfYearlyAccBalanceStatement statement;
    private boolean passwordProtectionEnabled;
    private String targetFilePath;

    public HalfYearlyAccBlncStatementGenerator(HalfYearlyAccBalanceStatement statement, String targetFilePath, boolean passwordProtectionEnabled) throws IOException, DocumentException {
        this.initializeFonts();
        this.statement = statement;
        this.targetFilePath = targetFilePath;
        this.passwordProtectionEnabled = passwordProtectionEnabled;
    }

    private void initializeFonts() throws IOException, DocumentException {
        this.baseFont = BaseFont.createFont(new ClassPathResource("fonts/RobotoCondensed-Regular.ttf").getPath(), BaseFont.IDENTITY_H, true);
        this.boldBaseFont = BaseFont.createFont(new ClassPathResource("fonts/RobotoCondensed-Bold.ttf").getPath(), BaseFont.IDENTITY_H, true);

        this.font = new Font(baseFont, 10);
        this.boldFont = new Font(boldBaseFont, 10);
        this.fontSmall = new Font(baseFont, 9);
        this.boldFontSmall = new Font(boldBaseFont, 9);
        this.boldFontUnderLiner = new Font(boldBaseFont, 11, Font.UNDERLINE);
    }

    public File generatePdf() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        File stFile = new File(targetFilePath);
        stFile.getParentFile().mkdirs();

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(stFile));

        document.setMargins(36, 36, 195, 80);

        if (passwordProtectionEnabled) encryptFile(writer);

        BcCertificateEvent event = new BcCertificateEvent(statement);
        writer.setPageEvent(event);

        document.open();

        Paragraph statementSummaryTitle = new Paragraph("STATEMENT SUMMARY",boldFont);
        statementSummaryTitle.setAlignment(Element.ALIGN_LEFT);
        statementSummaryTitle.setSpacingBefore(20f);
        document.add(statementSummaryTitle);

        PdfPTable statementSummary = getStatementSummary();
        statementSummary.setSpacingBefore(10f);
        statementSummary.setSpacingAfter(10f);
        document.add(statementSummary);

        Paragraph statementTitle = new Paragraph("TRANSACTIONS OF 01-Jan-2023 TO 31-Jan-2023",boldFont);
        statementTitle.setAlignment(Element.ALIGN_LEFT);
        statementTitle.setSpacingBefore(20f);
        document.add(statementTitle);

        PdfPTable statementList = this.getStatementList();
        statementList.setSpacingBefore(10f);
        document.add(statementList);

        PdfPTable rewardPointSummery = this.getRewardPointSummery();
//        rewardPointSummery.setSpacingAfter(20f);
        rewardPointSummery.setSpacingBefore(20f);
        document.add(rewardPointSummery);

        PdfPTable cautionSection = this.getCautionSection();

        float remainingSpace = writer.getVerticalPosition(true)-document.bottom(document.bottomMargin());

        if (cautionSection.getTotalHeight() > remainingSpace) {
            document.newPage();
        }

        cautionSection.writeSelectedRows(0, -1,
                34,
                cautionSection.getTotalHeight() + document.bottom(document.bottomMargin()) - 50,
                writer.getDirectContent());

        document.close();

        stFile.setReadable(true);
        stFile.setExecutable(false);

        return stFile;
    }

    private static String formatDate(LocalDate date) {
        if (date != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
            return dateTimeFormatter.format(date);
        }
        return null;
    }
    
    private PdfPTable getStatementList() throws DocumentException {

        PdfPTable pdfPTable = new PdfPTable(6);
        pdfPTable.setWidths(new int[]{10,35, 10, 15, 15, 15});
        pdfPTable.setWidthPercentage(100);

        Paragraph paragraph = new Paragraph("DATE", boldFont);
        PdfPCell pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        paragraph = new Paragraph("PARTICULARS", boldFont);
        pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        paragraph = new Paragraph("CHQ.NO", boldFont);
        pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        paragraph = new Paragraph("WITHDRAW", boldFont);
        pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        paragraph = new Paragraph("DEPOSIT", boldFont);
        pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        paragraph = new Paragraph("BALANCE", boldFont);
        pdfPCell = new PdfPCell(paragraph);
        pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,BaseColor.WHITE,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

        for (int i = 0; i <30 ; i++){
            BaseColor rowColor = null;
            if(i%2==0){
                rowColor = new BaseColor(242, 242, 242);
            }else {
                rowColor = BaseColor.WHITE;
            }

            paragraph = new Paragraph(statement.accountNumber, font);
            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

            paragraph = new Paragraph(statement.schemeType, font);
            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

            paragraph = new Paragraph(statement.accountCurrencyCode, font);
            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

            paragraph = new Paragraph(statement.accountCurrencyCode, font);
            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

            paragraph = new Paragraph(statement.accountCurrencyCode, font);
            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));

            String commaSeperatedBalance = StatementUtil.commaSeparatedAmount(this.statement.balanceAsOnDate);
            paragraph = new Paragraph(Objects.equals(statement.getBalanceIndicator(), "CREDIT") ? "(+) " + commaSeperatedBalance : "(-) " + commaSeperatedBalance, font);

            pdfPCell = new PdfPCell(paragraph);
            pdfPTable.addCell(applyStatementListHeaderStyle(pdfPCell,rowColor,Element.ALIGN_CENTER,Element.ALIGN_CENTER));
        }
        return pdfPTable;
    }
    
    private static PdfPCell applyStatementListHeaderStyle(PdfPCell pdfPCell, BaseColor backgroundColor, int horizontalAlignment, int verticalAlignment){
        pdfPCell.setUseAscender(true);
        pdfPCell.setFixedHeight(18f);
        pdfPCell.setPadding(5f);
        pdfPCell.setBackgroundColor(backgroundColor);
        pdfPCell.setHorizontalAlignment(horizontalAlignment);
        pdfPCell.setVerticalAlignment(verticalAlignment);
        return pdfPCell;
    }

    private PdfPTable getStatementSummary(){

        PdfPTable statementSummaryTable = new PdfPTable(4);
        statementSummaryTable.getDefaultCell().setBorder(0);
        statementSummaryTable.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell(new Paragraph("CUSTOMER ID" ,boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("04881404",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("STATEMENT PERIOD",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("29-Dec-2018 TO 12-Jan-2019",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("ACCOUNT NUMBER",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("04881404",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("OPENNING BALANCE",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("29-Dec-2018 TO 12-Jan-2019",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("BRANCH NAME",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("04881404",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("CLOSING BALANCE",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("29-Dec-2018 TO 12-Jan-2019",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("ACCOUNT TYPE",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("04881404",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("TOTAL WITHDRWAL",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("29-Dec-2018 TO 12-Jan-2019",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("CURRENCY",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("04881404",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("TOTAL DEPOSITS",boldFontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setBackgroundColor(new BaseColor(242, 242, 242));
        statementSummaryTable.addCell(cell1);

        cell1 = new PdfPCell(new Paragraph("29-Dec-2018 TO 12-Jan-2019",fontSmall));
        applyStatementSummaryCellStyle(cell1);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        statementSummaryTable.addCell(cell1);

        return statementSummaryTable;
    }
    private static void applyStatementSummaryCellStyle(PdfPCell cell) {
        cell.setBorder(15);
        cell.setBorderColor(new BaseColor(166, 166, 166));
        cell.setPaddingLeft(5.03f);
        cell.setPaddingRight(5.03f);
        cell.setPaddingTop(5.03f);
        cell.setPaddingBottom(6.03f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
    }

    private PdfPTable getRewardPointSummery(){
        PdfPTable table = new PdfPTable(3);
        PdfPTable mainTable = new PdfPTable(1);
        PdfPTable outerTable = new PdfPTable(1);
        table.setWidthPercentage(70);
        try {
            table.setWidths(new int[]{50,10,40});
            mainTable.setWidths(new int[]{100});
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        table.setTotalWidth(400);
        table.setLockedWidth(true);

        mainTable.setTotalWidth(400);
        mainTable.setLockedWidth(true);



        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase("Reward Point for Ace No", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_LEFT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase(":", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_RIGHT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase("1501104881404001", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_CENTER,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reward Points Earned in June 23", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_LEFT,BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase(":", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_RIGHT,BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase("10", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_CENTER,BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reward Points Redeemed in June 23", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_LEFT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase(":", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_RIGHT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase("0", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_CENTER,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reward Points Expired in June 23", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_LEFT, BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase(":", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_RIGHT,BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase("75", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_CENTER,BaseColor.WHITE);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reward Points Available", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_LEFT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase(":", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_RIGHT,new BaseColor(242, 242, 242));
        table.addCell(cell);

        cell.setPhrase(new Phrase("674", font));
        applyRewardPointSummeryCellStyle(cell,Element.ALIGN_CENTER,new BaseColor(242, 242, 242));
        table.addCell(cell);


        PdfPCell mainTableTitleCell = new PdfPCell();
        Paragraph rewardPointSummaryTitle = new Paragraph("Reward Point Summary",boldFont);
        rewardPointSummaryTitle.setAlignment(Element.ALIGN_CENTER);
//        rewardPointSummaryTitle.setSpacingBefore(40f);
        rewardPointSummaryTitle.setSpacingAfter(10f);
        mainTableTitleCell.addElement(rewardPointSummaryTitle);
        mainTableTitleCell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(mainTableTitleCell);

        PdfPCell mainTableCell = new PdfPCell();
        mainTableCell.addElement(table);
        mainTableCell.setBorder(Rectangle.NO_BORDER);
        mainTableCell.setVerticalAlignment(Element.ALIGN_CENTER);
        mainTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        mainTableCell.setCellEvent(new DottedCell());
        mainTableCell.setUseVariableBorders(true);
        mainTable.addCell(mainTableCell);


        PdfPCell outerTableCell = new PdfPCell();
        outerTableCell.addElement(mainTable);
        outerTableCell.setBorder(Rectangle.NO_BORDER);
        outerTable.addCell(outerTableCell);
        return outerTable;
    }

    private static void applyRewardPointSummeryCellStyle(PdfPCell cell, int horizontalAlignment, BaseColor color) {
        cell.setPadding(6);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBackgroundColor(color);

    }
    
    private PdfPTable getCautionSection() throws DocumentException {
        PdfPTable cautionSection = new PdfPTable(1);
        cautionSection.setWidths(new int[]{100});
        cautionSection.setTotalWidth(527);
        cautionSection.setLockedWidth(true);
        cautionSection.getDefaultCell().setFixedHeight(40);

        PdfPCell pdfPCell0 = new PdfPCell();
        pdfPCell0.setPaddingTop(10f); // Space before the cell
        pdfPCell0.setPaddingBottom(15f);

        Paragraph paragraph = new Paragraph("THANK YOU FOR BANKING WITH BRAC BANK LIMITED.", boldFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        pdfPCell0.addElement(paragraph);

        paragraph = new Paragraph("THIS IS A COMPUTER-GENERATED STATEMENT AND DOES NOT REQUIRE ANY SIGNATURE.", boldFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        pdfPCell0.addElement(paragraph);

        paragraph = new Paragraph("**** END OF STATEMENT ****", boldFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        pdfPCell0.addElement(paragraph);

        pdfPCell0.setBorder(Rectangle.BOX); // Set the border to a black box around the cell
        pdfPCell0.setBorderColor(BaseColor.BLACK); // Set the border color to black
        pdfPCell0.setBorderWidth(1f); // Set the border width

        cautionSection.addCell(pdfPCell0);

        //empty cell
        PdfPCell emptyPCell1 = new PdfPCell();
        emptyPCell1.setBorder(Rectangle.NO_BORDER);
        paragraph = new Paragraph("\n\n");
        emptyPCell1.addElement(paragraph);
        cautionSection.addCell(emptyPCell1);

        PdfPCell pdfPCell1 = new PdfPCell();
        pdfPCell1.setPadding(10f); // Space before the cell
        // Creating the first paragraph
        paragraph = new Paragraph("You can purchase or transfer money through online/e-commerce by using your BRAC Bank card where card number, expiry date, three-digit CVV number and OTP received through SMS are required. Card PIN is not required to make online/ecommerce transaction.", font);
        paragraph.setSpacingBefore(10f);
        paragraph.setSpacingAfter(10f);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        pdfPCell1.addElement(paragraph);
        // Creating the second paragraph
        paragraph = new Paragraph("For your convenience, local online gateway is always open. Please do not share card information (card number, card expiry date, CVV number, OTP, and PIN) to anybody, not even with bank official or call center agent.", font);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        paragraph.setSpacingBefore(10f);
        paragraph.setSpacingAfter(10f);
        pdfPCell1.addElement(paragraph);
        // Set the border color and width for the cell
        pdfPCell1.setBorder(Rectangle.BOX); // Set the border to a black box around the cell
        pdfPCell1.setBorderColor(BaseColor.BLACK); // Set the border color to black
        pdfPCell1.setBorderWidth(1f); // Set the border width

        cautionSection.addCell(pdfPCell1);

        return cautionSection;
    }

    private void encryptFile(PdfWriter writer) throws DocumentException {
        writer.setEncryption(statement.accountNumber.getBytes(), statement.accountNumber.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
    }
}
