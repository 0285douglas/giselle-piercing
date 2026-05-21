package com.gisellepiercing.application.controller;

import com.gisellepiercing.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Long> checkout(@RequestParam Long userId) {
        return ResponseEntity.ok(service.checkout(userId));
    }
}