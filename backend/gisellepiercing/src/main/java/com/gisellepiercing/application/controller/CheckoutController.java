package com.gisellepiercing.application.controller;

import com.gisellepiercing.dto.request.CheckoutRequestDTO;
import com.gisellepiercing.security.CustomAuthenticationToken;
import com.gisellepiercing.service.CheckoutService;
import com.mercadopago.resources.payment.Payment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<Payment> checkout(@Valid @RequestBody CheckoutRequestDTO dto, Principal principal) {
        CustomAuthenticationToken auth = (CustomAuthenticationToken) principal;
        Payment payment = checkoutService.processarCheckout(auth.getUserId(), auth.getName(), dto);
        return ResponseEntity.ok(payment);
    }
}