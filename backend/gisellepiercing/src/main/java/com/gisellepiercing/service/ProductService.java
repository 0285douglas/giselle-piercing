package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.ProductNotFoundException;
import com.gisellepiercing.dto.request.ProductRequestDTO;
import com.gisellepiercing.dto.response.ProductResponseDTO;
import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final EmailService emailService;

    public ProductService(ProductRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<Product> findProducts(String category, String material) {
        log.info("Searching products by category={} and material={}", category, material);
        return repository.findProducts(category, material);
    }

    public Product findById(Long id) {
        log.info("Searching product by id={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found id={}", id);
                    return new ProductNotFoundException("Product not found with id: " + id);
                });
    }

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        log.info("Creating product name={}", request.getName());
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setMaterial(request.getMaterial());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinimumStock(request.getMinimumStock());

        Product savedProduct = repository.save(product);
        validateStock(savedProduct);
        return toResponse(savedProduct);
    }

    public Product updateProduct(Long id, Product product) {
        log.info("Updating product id={}", id);
        findById(id);
        validateStock(product);
        return repository.update(id, product);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product id={}", id);
        findById(id);
        repository.delete(id);
    }

    private void validateStock(Product product) {
        if (product.getStockQuantity() <= product.getMinimumStock()) {
            log.warn("Low stock detected product={} quantity={}", product.getName(), product.getStockQuantity());
            emailService.sendLowStockAlert(product.getName(), product.getStockQuantity());
        }
    }

    private ProductResponseDTO toResponse(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .material(product.getMaterial())
                .stockQuantity(product.getStockQuantity())
                .minimumStock(product.getMinimumStock())
                .build();
    }

    public void decreaseStock(Long productId, Integer quantity) {
        Product product = findById(productId);

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        repository.decreaseStock(productId, quantity);
        Product updatedProduct = findById(productId);
        validateStock(updatedProduct);
        log.info("Stock updated productId={} quantityRemoved={}", productId, quantity);
    }
}