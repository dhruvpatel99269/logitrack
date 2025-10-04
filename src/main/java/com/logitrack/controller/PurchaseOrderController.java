package com.logitrack.controller;

import com.logitrack.dto.PurchaseOrderDto;
import com.logitrack.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    //@PreAuthorize("hasRole('PURCHASE') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrderDto> createOrder(@RequestBody PurchaseOrderDto dto) {
        return ResponseEntity.ok(purchaseOrderService.createOrder(dto));
    }

    @GetMapping
    //@PreAuthorize("hasRole('PURCHASE') or hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseOrderDto>> getAllOrders() {
        return ResponseEntity.ok(purchaseOrderService.getAllOrders());
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('PURCHASE') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrderDto> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.getOrder(id));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        purchaseOrderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
