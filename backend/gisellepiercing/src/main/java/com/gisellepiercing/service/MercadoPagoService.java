package com.gisellepiercing.service;

import com.gisellepiercing.model.PaymentMethod;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class MercadoPagoService {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public Payment createPayment(Long orderId, BigDecimal amount, String email, PaymentMethod method) throws Exception {
        PaymentClient client = new PaymentClient();
        String paymentMethod;
        switch (method) {
            case PIX -> paymentMethod = "pix";
            case BOLETO -> paymentMethod = "bolbradesco";
            case CREDIT_CARD -> throw new RuntimeException("Card payment needs tokenization");
            default -> throw new RuntimeException("Invalid payment method");
        }
        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(amount)
                .description("Giselle Piercing Order")
                .paymentMethodId(paymentMethod)
                .externalReference(orderId.toString())
                .payer(PaymentPayerRequest.builder().email(email).build())
                .build();
        return client.create(request);
    }
}