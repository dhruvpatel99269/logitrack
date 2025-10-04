package com.logitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private String productName;
    private Integer totalPurchased;
    private Integer totalSold;
    private Integer currentStock;
    private Double totalPurchaseValue;
    private Double totalSalesValue;
    private Double profitOrLoss;
}
