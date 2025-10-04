package com.logitrack.service;

import com.logitrack.dto.ProductDto;
import com.logitrack.entity.Product;
import com.logitrack.repository.ProductRepository;
import com.logitrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Create a new product
    public ProductDto createProduct(ProductDto dto) {
        if(productRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Product with name already exists");
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product saved = productRepository.save(product);
        dto.setId(saved.getId());
        return dto;
    }

    // Get product by ID
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapToDto(product);
    }

    // Get all products
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update product
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        productRepository.save(product);
        return mapToDto(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    // Helper to map entity to DTO
    private ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
