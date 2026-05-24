package com.gisellepiercing.application.controller;

import com.gisellepiercing.dto.request.CheckoutRequestDTO;
import com.gisellepiercing.dto.response.CheckoutResponseDTO;
import com.gisellepiercing.security.CustomAuthenticationToken;
import com.gisellepiercing.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> checkout(@RequestBody CheckoutRequestDTO request, Principal principal) throws Exception {
        CustomAuthenticationToken auth = (CustomAuthenticationToken) principal;
        return ResponseEntity.ok(service.checkout(auth.getUserId(), auth.getName(), request));
    }
}