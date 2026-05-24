package com.gisellepiercing.service;

import com.gisellepiercing.dto.request.CheckoutRequestDTO;
import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.dto.response.CheckoutResponseDTO;
import com.gisellepiercing.model.Order;
import com.gisellepiercing.model.OrderStatus;
import com.gisellepiercing.model.PaymentMethod;
import com.gisellepiercing.repository.OrderRepository;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckoutService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MercadoPagoService mercadoPagoService;

    public CheckoutService(CartService cartService, OrderRepository orderRepository, MercadoPagoService mercadoPagoService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.mercadoPagoService = mercadoPagoService;
    }

    public CheckoutResponseDTO checkout(Long userId, String email, CheckoutRequestDTO request) throws Exception {
        CartResponseDTO cart = cartService.getCart(userId);
        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(cart.getTotal());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        Long orderId = orderRepository.createOrder(order);
        for (CartItemResponseDTO item : cart.getItems()) {
            orderRepository.createOrderItem(orderId, item);
        }
        Payment payment = mercadoPagoService.createPayment(orderId, cart.getTotal(), email, request.getPaymentMethod());
        String qrCode = null;
        String qrCodeBase64 = null;
        String boletoUrl = null;
        if (request.getPaymentMethod() == PaymentMethod.PIX) {
            qrCode = payment.getPointOfInteraction().getTransactionData().getQrCode();
            qrCodeBase64 = payment.getPointOfInteraction().getTransactionData().getQrCodeBase64();
        }
        if (request.getPaymentMethod() == PaymentMethod.BOLETO) {
            boletoUrl = payment.getTransactionDetails().getExternalResourceUrl();
        }
        log.info("Payment created orderId={} method={}", orderId, request.getPaymentMethod());
        return CheckoutResponseDTO.builder().orderId(orderId).qrCode(qrCode).qrCodeBase64(qrCodeBase64).boletoUrl(boletoUrl).status(payment.getStatus()).build();
    }
}