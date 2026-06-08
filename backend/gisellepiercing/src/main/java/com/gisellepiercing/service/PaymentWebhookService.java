package com.gisellepiercing.service;

import com.gisellepiercing.model.OrderItem;
import com.gisellepiercing.model.OrderStatus;
import com.gisellepiercing.repository.OrderRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentWebhookService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    public PaymentWebhookService(
            OrderRepository orderRepository,
            ProductService productService,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    public void processPayment(Long paymentId) throws Exception {

        PaymentClient client = new PaymentClient();

        Payment payment = client.get(paymentId);

        log.info(
                "Webhook received paymentId={} status={}",
                paymentId,
                payment.getStatus()
        );

        if (!"approved".equals(payment.getStatus())) {
            return;
        }

        Long orderId =
                Long.parseLong(payment.getExternalReference());

        processApprovedOrder(orderId);
    }

    // Endpoint para simular pagamento aprovado (apenas para testes)
    public void simulateApprovedPayment(Long orderId) {

        log.info(
                "Simulating approved payment orderId={}",
                orderId
        );

        processApprovedOrder(orderId);
    }

    private void processApprovedOrder(Long orderId) {

        orderRepository.updateStatus(
                orderId,
                OrderStatus.PAID.name()
        );

        List<OrderItem> items =
                orderRepository.findItemsByOrderId(orderId);

        for (OrderItem item : items) {

            productService.decreaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        Long userId =
                orderRepository.findUserIdByOrderId(orderId);

        cartService.clearCartByUserId(userId);

        log.info(
                "Order approved orderId={} stockUpdated=true cartCleared=true",
                orderId
        );
    }
}