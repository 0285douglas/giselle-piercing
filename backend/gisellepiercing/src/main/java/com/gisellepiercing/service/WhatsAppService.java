package com.gisellepiercing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    @Value("${zapi.instance-id}")
    private String instanceId;

    @Value("${zapi.token}")
    private String token;

    @Value("${zapi.client-token}")
    private String clientToken;

    @Value("${zapi.phone}")
    private String phone;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendLowStockAlert(String productName, Integer quantity) {

        String url =
                "https://api.z-api.io/instances/" +
                        instanceId +
                        "/token/" +
                        token +
                        "/send-text";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-Token", clientToken);

        Map<String, Object> body = Map.of(
                "phone", phone,
                "message",
                "⚠️ Estoque baixo\nProduto: " +
                        productName +
                        "\nQuantidade: " +
                        quantity
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                url,
                entity,
                String.class
        );
    }
}