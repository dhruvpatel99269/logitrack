package com.logitrack.service;

import com.logitrack.dto.SalesOrderDto;
import com.logitrack.dto.SalesOrderItemDto;
import com.logitrack.entity.SalesOrder;
import com.logitrack.entity.SalesOrderItem;
import com.logitrack.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    public SalesOrderDto createSalesOrder(SalesOrderDto dto) {
        SalesOrder order = new SalesOrder();
        order.setCustomerName(dto.getCustomerName());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());

        List<SalesOrderItem> items = dto.getItems().stream().map(itemDto -> {
            SalesOrderItem item = new SalesOrderItem();
            item.setProductName(itemDto.getProductName());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());
            item.setSalesOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        SalesOrder saved = salesOrderRepository.save(order);
        return mapToDto(saved);
    }

    public List<SalesOrderDto> getAllSalesOrders() {
        return salesOrderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SalesOrderDto mapToDto(SalesOrder order) {
        List<SalesOrderItemDto> items = order.getItems().stream()
                .map(i -> new SalesOrderItemDto(i.getId(), i.getProductName(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());
        return new SalesOrderDto(order.getId(), order.getCustomerName(), order.getOrderDate(), order.getTotalAmount(), items);
    }
}
