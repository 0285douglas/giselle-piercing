package com.gisellepiercing.application.controller;

import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.security.CustomAuthenticationToken;
import com.gisellepiercing.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestParam Long productId, @RequestParam Integer quantity, Principal principal) {
        CustomAuthenticationToken auth = (CustomAuthenticationToken) principal;
        service.addItem(auth.getUserId(), productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Principal principal) {
        CustomAuthenticationToken auth = (CustomAuthenticationToken) principal;
        return ResponseEntity.ok(service.getCart(auth.getUserId()));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id) {
        service.removeItem(id);
        return ResponseEntity.noContent().build();
    }
}