package com.logitrack.service;

import com.logitrack.dto.ReportDto;
import com.logitrack.entity.Product;
import com.logitrack.entity.PurchaseOrder;
import com.logitrack.entity.PurchaseOrderItem;
import com.logitrack.entity.SalesOrder;
import com.logitrack.entity.SalesOrderItem;
import com.logitrack.repository.ProductRepository;
import com.logitrack.repository.PurchaseOrderRepository;
import com.logitrack.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SalesOrderRepository salesOrderRepository;

    public List<ReportDto> generateReport() {

        // Fetch all required data
        List<Product> products = productRepository.findAll();
        List<PurchaseOrder> purchases = purchaseOrderRepository.findAll();
        List<SalesOrder> sales = salesOrderRepository.findAll();

        // Map to hold aggregated data
        Map<String, ReportDto> reportMap = new HashMap<>();

        // Process purchase data
        for (PurchaseOrder po : purchases) {
            for (PurchaseOrderItem item : po.getItems()) {
                reportMap.putIfAbsent(item.getProductName(), new ReportDto(item.getProductName(), 0, 0, 0, 0.0, 0.0, 0.0));
                ReportDto report = reportMap.get(item.getProductName());
                report.setTotalPurchased(report.getTotalPurchased() + item.getQuantity());
                report.setTotalPurchaseValue(report.getTotalPurchaseValue() + (item.getQuantity() * item.getPrice()));
            }
        }

        // Process sales data
        for (SalesOrder so : sales) {
            for (SalesOrderItem item : so.getItems()) {
                reportMap.putIfAbsent(item.getProductName(), new ReportDto(item.getProductName(), 0, 0, 0, 0.0, 0.0, 0.0));
                ReportDto report = reportMap.get(item.getProductName());
                report.setTotalSold(report.getTotalSold() + item.getQuantity());
                report.setTotalSalesValue(report.getTotalSalesValue() + (item.getQuantity() * item.getPrice()));
            }
        }

        // Calculate current stock & profit/loss
        for (Product product : products) {
            reportMap.putIfAbsent(product.getName(), new ReportDto(product.getName(), 0, 0, product.getQuantity(), 0.0, 0.0, 0.0));
        }

        reportMap.values().forEach(report -> {
            report.setCurrentStock(report.getTotalPurchased() - report.getTotalSold());
            report.setProfitOrLoss(report.getTotalSalesValue() - report.getTotalPurchaseValue());
        });

        return reportMap.values().stream()
                .sorted(Comparator.comparing(ReportDto::getProductName))
                .collect(Collectors.toList());
    }
}
