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
    public static Font boldFont2;
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
            boldFont2 = new Font(boldBaseFont, 9);

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

                Chunk chunkTitle= new Chunk("Ref: ", boldFont);
                Chunk chunkValue = new Chunk("123-ABC-456",font);

                Phrase phrase = new Phrase();
                phrase.add(chunkTitle);
                phrase.add(chunkValue);

                addHeaderContent(pdfPTable,phrase);

                chunkTitle= new Chunk("ISSUE DATE: ", boldFont);
                chunkValue = new Chunk(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),font);
                phrase = new Phrase();
                phrase.add(chunkTitle);
                phrase.add(chunkValue);
                addHeaderContent(pdfPTable,phrase);

                Paragraph customerTitle = new Paragraph(statement.customerTitle, boldFont);
                PdfPCell customerTitleCell = new PdfPCell(customerTitle);
                customerTitleCell.setPaddingTop(10f);
                customerTitleCell.setPaddingBottom(10f);
                customerTitleCell.setBorder(Rectangle.NO_BORDER);
                pdfPTable.addCell(customerTitleCell);

                Paragraph address = new Paragraph(statement.customerPermanentAddress, font);
                PdfPCell customerAddress = new PdfPCell(address);
                customerAddress.setBorder(Rectangle.NO_BORDER);
                pdfPTable.addCell(customerAddress);

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

    private void addHeaderContent(PdfPTable table, Phrase phrase) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(phrase);
        PdfPCell pdfPCell = new PdfPCell(paragraph);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pdfPCell.setPaddingTop(5f);
        table.addCell(pdfPCell);
    }

    private void addFooter(PdfWriter writer) {
        try {
            PdfPTable footer = new PdfPTable(2);
            footer.setWidths(new int[]{50,50});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);

            // Second Cell
            PdfPCell pdfPCell1 = new PdfPCell();
            pdfPCell1.setBorder(Rectangle.TOP);
            pdfPCell1.setBorderColor(BaseColor.BLACK);
            pdfPCell1.setPaddingTop(10f); // Space before the cell
            pdfPCell1.setPaddingBottom(10f);

            PdfPCell pdfPCell2 = new PdfPCell();
            pdfPCell2.setBorder(Rectangle.TOP);
            pdfPCell2.setBorderColor(BaseColor.BLACK);
            pdfPCell2.setPaddingTop(10f); // Space before the cell
            pdfPCell2.setPaddingBottom(10f);

            Paragraph paragraph1 = new Paragraph(statement.customerTitle,boldFont2);
            paragraph1.setAlignment(Element.ALIGN_LEFT);
            pdfPCell1.addElement(paragraph1);
            footer.addCell(pdfPCell1);

            String pageString = "Page: " + writer.getCurrentPageNumber();
            Paragraph paragraph2 = new Paragraph(pageString,font2);
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