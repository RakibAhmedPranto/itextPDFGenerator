package com.example.testPDF;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

class DottedCell implements PdfPCellEvent {
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        float lineWidth = 1f;
        canvas.setLineWidth(lineWidth);
//        canvas.setColorStroke(new BaseColor(166, 166, 166));
        canvas.setLineDash(new float[] {3.0f, 3.0f}, 0);
        canvas.moveTo(position.getLeft(), position.getBottom());
        canvas.lineTo(position.getRight(), position.getBottom());
        canvas.lineTo(position.getRight(), position.getTop());
        canvas.lineTo(position.getLeft(), position.getTop());
        canvas.lineTo(position.getLeft(), position.getBottom());
        canvas.stroke();
    }
}
