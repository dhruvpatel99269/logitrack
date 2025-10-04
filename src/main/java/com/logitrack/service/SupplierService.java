package com.logitrack.service;

import com.logitrack.dto.SupplierDto;
import com.logitrack.entity.Supplier;
import com.logitrack.repository.SupplierRepository;
import com.logitrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    // Create supplier
    public SupplierDto createSupplier(SupplierDto dto) {
        if (supplierRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Supplier with this name already exists");
        }
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());

        Supplier saved = supplierRepository.save(supplier);
        dto.setId(saved.getId());
        return dto;
    }

    // Get supplier by ID
    public SupplierDto getSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        return mapToDto(supplier);
    }

    // Get all suppliers
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update supplier
    public SupplierDto updateSupplier(Long id, SupplierDto dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());

        supplierRepository.save(supplier);
        return mapToDto(supplier);
    }

    // Delete supplier
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found");
        }
        supplierRepository.deleteById(id);
    }

    // Map entity to DTO
    private SupplierDto mapToDto(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress()
        );
    }
}
