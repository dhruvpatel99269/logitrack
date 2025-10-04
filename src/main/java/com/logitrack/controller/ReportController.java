package com.logitrack.controller;

import com.logitrack.dto.ReportDto;
import com.logitrack.service.ReportExportService;
import com.logitrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    
    private final ReportService reportService;
    private final ReportExportService exportService;


    @GetMapping
    public ResponseEntity<List<ReportDto>> getReport() {
        return ResponseEntity.ok(reportService.generateReport());
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPDF() {
        List<ReportDto> reports = reportService.generateReport();
        ByteArrayInputStream pdf = exportService.exportToPDF(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=inventory_report.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf.readAllBytes());
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() {
        List<ReportDto> reports = reportService.generateReport();
        ByteArrayInputStream excel = exportService.exportToExcel(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=inventory_report.xlsx")
                .contentType(org.springframework.http.MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel.readAllBytes());
    }

}
