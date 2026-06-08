package com.gisellepiercing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendLowStockAlert(String productName, Integer quantity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("0285douglas@gmail.com");
        message.setSubject("Alerta de estoque: " + productName + " com estoque baixo");
        message.setText("O produto " + productName + " está com estoque baixo. Quantidade atual: " + quantity);
        mailSender.send(message);
    }
}