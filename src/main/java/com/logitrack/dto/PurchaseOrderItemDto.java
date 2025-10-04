package com.logitrack.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {

    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;
}
