package com.logitrack.controller;

import com.logitrack.dto.SalesOrderDto;
import com.logitrack.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SalesOrderDto> createOrder(@RequestBody SalesOrderDto dto) {
        return ResponseEntity.ok(salesOrderService.createSalesOrder(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SalesOrderDto>> getAllOrders() {
        return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
    }
}
