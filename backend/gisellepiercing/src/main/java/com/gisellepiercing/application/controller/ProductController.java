package com.gisellepiercing.application.controller;

import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String material
    ) {
        return ResponseEntity.ok(repository.findProducts(category, material));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(repository.save(product));
    }
}