package com.gisellepiercing.application.controller;

import com.gisellepiercing.service.PaymentWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final PaymentWebhookService service;

    public WebhookController(PaymentWebhookService service) {
        this.service = service;
    }

    @PostMapping("/mercado-pago")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) throws Exception {
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        Long paymentId = Long.valueOf(data.get("id").toString());
        service.processPayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mercado-pago/simulate")
    public ResponseEntity<String> simulate(@RequestParam Long orderId) {
        service.simulateApprovedPayment(orderId);
        return ResponseEntity.ok("Pagamento simulado com sucesso");
    }
}