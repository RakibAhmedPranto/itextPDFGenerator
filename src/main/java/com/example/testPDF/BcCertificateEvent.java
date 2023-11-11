package com.example.testPDF;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class BcCertificateEvent extends PdfPageEventHelper {

    public static BaseFont boldBaseFont;
    public static BaseFont baseFont;
    public static Font font;
    public static Font font2;
    public static Font boldFont;
    public Boolean isHardcopyRequired = false;

    HalfYearlyAccBalanceStatement statement;
    public BcCertificateEvent(HalfYearlyAccBalanceStatement statement) {
        this.statement = statement;
        this.isHardcopyRequired = statement.isHardcopyRequired;
    }


    static {
        try {
            baseFont = BaseFont.createFont(new ClassPathResource("fonts/RobotoCondensed-Regular.ttf").getPath(), BaseFont.IDENTITY_H, true);
            boldBaseFont = BaseFont.createFont(new ClassPathResource("fonts/RobotoCondensed-Bold.ttf").getPath(), BaseFont.IDENTITY_H, true);

            font = new Font(baseFont, 10);
            font2 = new Font(baseFont, 9);
            boldFont = new Font(boldBaseFont, 10);

        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfTemplate t;
    private Image total;

    public void onOpenDocument(PdfWriter writer, Document document) {

        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        document.setMargins(36, 36, 80, 80);
        addHeader(writer);
        if(!isHardcopyRequired) addFooter(writer);
    }

    public void addHeader(PdfWriter writer) {
        try {
            PdfPTable header = new PdfPTable(2);
            PdfPCell pdfPCell = new PdfPCell();


            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setBorder(0);

//            if (!isHardcopyRequired && writer.getCurrentPageNumber() != 1) {
//                Image logo = Image.getInstance("images/BBL.png");
//                pdfPCell.addElement(logo);
//            }
//
//            pdfPCell.setBorder(0);
//            header.addCell(pdfPCell);

            Image logo = Image.getInstance(new ClassPathResource("images/BBL.png").getPath());
            pdfPCell.addElement(logo);
            pdfPCell.setBorder(0);
            header.addCell(pdfPCell);

            if (writer.getCurrentPageNumber() == 1) {
                PdfPTable pdfPTable_0 = new PdfPTable(1);
                addHeaderContent(pdfPTable_0, "BRAC Bank Limited", boldFont);
                addHeaderContent(pdfPTable_0, "Head office, Anik Tower", font);
                addHeaderContent(pdfPTable_0, "220/B, Tejgaon Gulshan Link Road", font);
                addHeaderContent(pdfPTable_0, "Tejgaon I/A, Dhaka 1208, Bangladesh", font);
                addHeaderContent(pdfPTable_0, "Phone     : +88 02 8801301-32, 9884292", font);
                addHeaderContent(pdfPTable_0, "Fax          : +88 02 9898910", font);
                addHeaderContent(pdfPTable_0, "SWIFT     : BRAKBDDH", font);
                addHeaderContent(pdfPTable_0, "E-mail     : enquiry@bracbank.com", font);
                addHeaderContent(pdfPTable_0, "Website  : www.bracbank.com", font);

                pdfPCell = new PdfPCell(pdfPTable_0);
                pdfPCell.setBorder(0);
                pdfPCell.setPaddingLeft(100);
                pdfPCell.setColspan(2);
                pdfPCell.setRowspan(2);
                header.addCell(pdfPCell);

                PdfPTable pdfPTable = new PdfPTable(1);
                addHeaderContent(pdfPTable, "Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")), font);
                addHeaderContent(pdfPTable, "Ref: Your Reference Number", font); // Change as required
                addHeaderContent(pdfPTable, "Name: Customer Name", font); // Change as required
                addHeaderContent(pdfPTable, "Address: Customer Address", font); // Change as required
                addHeaderContent(pdfPTable, "Customer ID: 123456789", font); // Change as required

                header.addCell(pdfPTable);
            }

            if (writer.getCurrentPageNumber() > 1) {
                // Only display the image for pages other than the first page
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(0);
                header.addCell(emptyCell);

                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(0);
                header.addCell(logoCell);
                header.getDefaultCell().setFixedHeight(40);
            }

            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            header.writeSelectedRows(0, -1, 34, 810, writer.getDirectContent());
        } catch (DocumentException | IOException documentException) {
            throw new ExceptionConverter(documentException);
        }
    }

    private void addHeaderContent(PdfPTable table, String content, Font font) {
        Paragraph paragraph = new Paragraph(content, font);
        PdfPCell pdfPCell = new PdfPCell(paragraph);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pdfPCell.setPaddingTop(5f);
        table.addCell(pdfPCell);
    }

    private void addFooter(PdfWriter writer) {
        try {
            PdfPTable footer = new PdfPTable(1);
            footer.setWidths(new int[]{100});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);

            // Second Cell
            PdfPCell pdfPCell2 = new PdfPCell();
            pdfPCell2.setBorder(Rectangle.TOP);
            pdfPCell2.setBorderColor(BaseColor.BLACK);
            pdfPCell2.setPaddingTop(10f); // Space before the cell
            pdfPCell2.setPaddingBottom(10f);

            String pageString = "Page: " + writer.getCurrentPageNumber();

            Paragraph paragraph2 = new Paragraph(pageString);
            paragraph2.setAlignment(Element.ALIGN_RIGHT);
            pdfPCell2.addElement(paragraph2);

            footer.addCell(pdfPCell2);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);

            footer.writeSelectedRows(0, -1, 34, 75, canvas);

            canvas.endMarkedContentSequence();
        } catch (DocumentException documentException) {
            throw new ExceptionConverter(documentException);
        }
    }

//    @Override
//    public void onCloseDocument(PdfWriter writer, Document document) {
//        int totalLength = String.valueOf(writer.getPageNumber()).length();
//        int totalWidth = totalLength * 5;
//        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT, new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)), totalWidth, 6, 0);
//    }

    private String getRefNumber(String cif) {
        String refNumber = "BBL-LOCHY-" + new SimpleDateFormat("yyddMM").format(Calendar.getInstance().getTime()) + "-"
                + cif;
        return refNumber;
    }
}