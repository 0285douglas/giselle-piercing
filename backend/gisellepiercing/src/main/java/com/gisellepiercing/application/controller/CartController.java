package com.gisellepiercing.application.controller;

import com.gisellepiercing.dto.cart.CartItem;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity) {
        service.addItem(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getCart(userId));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id) {
        service.removeItem(id);
        return ResponseEntity.noContent().build();
    }
}