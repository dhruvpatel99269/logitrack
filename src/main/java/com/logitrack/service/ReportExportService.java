package com.logitrack.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.logitrack.dto.ReportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportExportService {

    // PDF Export
    public ByteArrayInputStream exportToPDF(List<ReportDto> reports) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);            

            Paragraph title = new Paragraph("Inventory Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 2, 2, 3, 3, 3});

            String[] headers = {"Product", "Purchased", "Sold", "Stock", "Purchase Value", "Sales Value", "Profit/Loss"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (ReportDto r : reports) {
                table.addCell(new Phrase(r.getProductName(), dataFont));
                table.addCell(String.valueOf(r.getTotalPurchased()));
                table.addCell(String.valueOf(r.getTotalSold()));
                table.addCell(String.valueOf(r.getCurrentStock()));
                table.addCell(String.format("%.2f", r.getTotalPurchaseValue()));
                table.addCell(String.format("%.2f", r.getTotalSalesValue()));
                table.addCell(String.format("%.2f", r.getProfitOrLoss()));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF report: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Excel Export
    public ByteArrayInputStream exportToExcel(List<ReportDto> reports) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventory Report");

            Row header = sheet.createRow(0);
            String[] columns = {"Product", "Purchased", "Sold", "Stock", "Purchase Value", "Sales Value", "Profit/Loss"};
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);


            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (ReportDto r : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getProductName());
                row.createCell(1).setCellValue(r.getTotalPurchased());
                row.createCell(2).setCellValue(r.getTotalSold());
                row.createCell(3).setCellValue(r.getCurrentStock());
                row.createCell(4).setCellValue(r.getTotalPurchaseValue());
                row.createCell(5).setCellValue(r.getTotalSalesValue());
                row.createCell(6).setCellValue(r.getProfitOrLoss());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel report: " + e.getMessage());
        }
    }
}
