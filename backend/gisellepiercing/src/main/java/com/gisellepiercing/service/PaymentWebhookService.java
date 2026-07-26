package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.OrderNotFoundException;
import com.gisellepiercing.application.exception.PaymentProcessingException;
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

    /**
     * Processa webhook de pagamento do Mercado Pago
     */
    public void processPayment(Long paymentId) throws Exception {

        log.info("Webhook recebido - paymentId={}", paymentId);

        PaymentClient client = new PaymentClient();

        Payment payment = client.get(paymentId);

        log.info(
                "Webhook processado - paymentId={} status={}",
                paymentId,
                payment.getStatus()
        );

        if (!"approved".equals(payment.getStatus())) {
            log.info("Pagamento não aprovado, não processando pedido - paymentId={} status={}", paymentId, payment.getStatus());
            return;
        }

        Long orderId =
                Long.parseLong(payment.getExternalReference());

        processApprovedOrder(orderId);
    }

    /**
     * Simula pagamento aprovado para testes (apenas para desenvolvimento)
     */
    public void simulateApprovedPayment(Long orderId) {

        log.info(
                "Simulando pagamento aprovado - orderId={}",
                orderId
        );

        processApprovedOrder(orderId);
    }

    /**
     * Processa pedido aprovado: atualiza status, reduz estoque e limpa carrinho
     */
    private void processApprovedOrder(Long orderId) {

        try {
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
                    "Pedido processado com sucesso - orderId={} estoque_atualizado=true carrinho_limpo=true",
                    orderId
            );

        } catch (Exception e) {
            log.error("Erro ao processar pedido aprovado - orderId={}", orderId, e);
            throw new PaymentProcessingException("Erro ao processar pagamento aprovado", e);
        }
    }
}