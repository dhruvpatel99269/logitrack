package com.logitrack.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {

    private Long id;
    private String supplierName;
    private Date orderDate;
    private Double totalAmount;
    private List<PurchaseOrderItemDto> items;
}
