package com.logitrack.service;

import com.logitrack.dto.PurchaseOrderDto;
import com.logitrack.dto.PurchaseOrderItemDto;
import com.logitrack.entity.PurchaseOrder;
import com.logitrack.entity.PurchaseOrderItem;
import com.logitrack.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    // Create Purchase Order
    public PurchaseOrderDto createOrder(PurchaseOrderDto dto) {
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierName(dto.getSupplierName());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());

        List<PurchaseOrderItem> items = dto.getItems().stream().map(itemDto -> {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProductName(itemDto.getProductName());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());
            item.setPurchaseOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        PurchaseOrder saved = purchaseOrderRepository.save(order);

        dto.setId(saved.getId());
        return dto;
    }

    // Get all orders
    public List<PurchaseOrderDto> getAllOrders() {
        return purchaseOrderRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Get order by ID
    public PurchaseOrderDto getOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
        return mapToDto(order);
    }

    // Delete order
    public void deleteOrder(Long id) {
        if (!purchaseOrderRepository.existsById(id)) {
            throw new RuntimeException("Purchase Order not found");
        }
        purchaseOrderRepository.deleteById(id);
    }

    // Mapper
    private PurchaseOrderDto mapToDto(PurchaseOrder order) {
        List<PurchaseOrderItemDto> items = order.getItems().stream().map(item ->
                new PurchaseOrderItemDto(item.getId(), item.getProductName(), item.getQuantity(), item.getPrice())
        ).collect(Collectors.toList());

        return new PurchaseOrderDto(order.getId(), order.getSupplierName(), order.getOrderDate(),
                order.getTotalAmount(), items);
    }
}
