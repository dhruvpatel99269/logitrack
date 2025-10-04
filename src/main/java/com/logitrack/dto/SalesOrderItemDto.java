package com.logitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemDto {
    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;
}
