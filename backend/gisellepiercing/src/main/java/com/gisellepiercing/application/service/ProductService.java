package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.ProductNotFoundException;
import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findProducts(String category, String material) {

        log.info("Searching products by category={} and material={}",
                category,
                material
        );

        return repository.findProducts(category, material);
    }

    public Product findById(Long id) {

        log.info("Searching product by id={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found id={}", id);

                    return new ProductNotFoundException(
                            "Product not found with id: " + id
                    );
                });
    }

    public Product createProduct(Product product) {

        log.info("Creating product name={}", product.getName());

        return repository.save(product);
    }

    public Product updateProduct(Long id, Product product) {

        log.info("Updating product id={}", id);

        findById(id);

        return repository.update(id, product);
    }

    public void deleteProduct(Long id) {

        log.info("Deleting product id={}", id);

        findById(id);

        repository.delete(id);
    }
}