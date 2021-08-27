package com.example.urzadmiasta.Application;

import com.example.urzadmiasta.Forms.FieldInForm;
import com.example.urzadmiasta.Forms.Form;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generator plików pdf
 */
public class PdfGenerator {

    /**
     * Generuje strumień tablicy bajtów zawierający plik pdf.
     * @param form formularz na podstawie którego stworzony ma być plik pdf
     * @return strumień tablicy bajtów
     */
    public static ByteArrayInputStream createPdf(Form form) {

        Document document = new Document(PageSize.A4, 20, 20, 30, 20);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            Font headFont = FontFactory.getFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, true, 40);
            Font titleFont = FontFactory.getFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, true, 24);
            Font normalFont = FontFactory.getFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, true, 16);
            Font smallFont = FontFactory.getFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, true, 10);

            PdfWriter.getInstance(document, out);

            document.open();

            Chunk chunk = new Chunk("Urząd Miasta", headFont);
            Paragraph para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            document.add(Chunk.NEWLINE);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            chunk = new Chunk("Miasto, " + date, normalFont);
            para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            chunk = new Chunk(form.getName(), titleFont);
            para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);
            chunk = new Chunk(form.getDescription(), normalFont);
            para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);


            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);


            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);


            for (FieldInForm fieldInForm : form.getFieldsInForm()) {
                Chunk chunk1 = new Chunk(fieldInForm.getField().getName(), normalFont);
                Chunk chunk2 = new Chunk(fieldInForm.getField().getDescription(), smallFont);
                Paragraph paragraph1 = new Paragraph();
                paragraph1.add(chunk1);
                paragraph1.add(Chunk.NEWLINE);
                paragraph1.add(chunk2);

                PdfPCell cell = new PdfPCell(paragraph1);
                cell.setFixedHeight(30);
                cell.setBorder(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.GRAY);
                cell.setColspan(2);
                table.addCell(cell);

                PdfPCell cell2 = new PdfPCell(new Phrase(fieldInForm.getValue()));
                cell2.setFixedHeight(30);
                cell2.setBorder(Rectangle.BOX);
                cell2.setColspan(2);
                table.addCell(cell2);
            }

            document.add(table);
            document.add(Chunk.NEWLINE);
            para = new Paragraph(new Chunk("........................................."));
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);
            para = new Paragraph(new Chunk("podpis"));
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.close();

        } catch (DocumentException ignored) {
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

}
