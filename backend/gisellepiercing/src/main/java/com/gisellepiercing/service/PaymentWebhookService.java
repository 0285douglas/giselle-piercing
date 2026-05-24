package com.gisellepiercing.service;

import com.gisellepiercing.model.OrderStatus;
import com.gisellepiercing.repository.OrderRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentWebhookService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    public PaymentWebhookService(OrderRepository orderRepository, ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    public void processPayment(Long paymentId) throws Exception {
        PaymentClient client = new PaymentClient();
        Payment payment = client.get(paymentId);
        if (!"approved".equals(payment.getStatus())) {
            return;
        }
        Long orderId = Long.parseLong(payment.getExternalReference());
        orderRepository.updateStatus(orderId, OrderStatus.PAID.name());
        log.info("Payment approved orderId={}", orderId);
    }
}