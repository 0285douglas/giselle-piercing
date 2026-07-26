package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.CartEmptyException;
import com.gisellepiercing.application.exception.InsufficientStockException;
import com.gisellepiercing.application.exception.OrderNotFoundException;
import com.gisellepiercing.application.exception.PaymentProcessingException;
import com.gisellepiercing.dto.request.CheckoutRequestDTO;
import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.model.Order;
import com.gisellepiercing.model.OrderStatus;
import com.gisellepiercing.model.Product;
import com.gisellepiercing.model.User;
import com.gisellepiercing.repository.OrderRepository;
import com.gisellepiercing.repository.UserRepository;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class CheckoutService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    public CheckoutService(UserRepository userRepository, OrderRepository orderRepository, CartService cartService, ProductService productService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    @Transactional
    public Payment processarCheckout(Long userId, String userEmail, CheckoutRequestDTO dto) {
        log.info("Processando checkout de metodo={} para o usuarioId={}", dto.getPaymentMethod(), userId);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com email: {}", userEmail);
                    return new OrderNotFoundException("Usuario nao encontrado");
                });

        CartResponseDTO cart = cartService.getCart(userId);

        validateCartNotEmpty(cart);
        validateProductStock(cart);

        PaymentCreateRequest paymentRequest = buildPaymentRequest(user, cart, dto);

        try {
            Payment payment = processPaymentWithMercadoPago(paymentRequest);
            Long orderId = createOrderFromCart(userId, cart, payment);

            log.info(
                    "Pedido gerado com sucesso - id={}, usuarioId={}, total={}",
                    orderId,
                    userId,
                    cart.getTotal()
            );

            return payment;

        } catch (Exception e) {
            log.error(
                    "Erro ao integrar com o Mercado Pago - usuarioId={}",
                    userId,
                    e
            );

            throw new PaymentProcessingException(
                    "Falha na operacao de pagamento: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Valida se carrinho não está vazio
     */
    private void validateCartNotEmpty(CartResponseDTO cart) {
        if (cart.getItems().isEmpty()) {
            log.warn("Tentativa de checkout com carrinho vazio");
            throw new CartEmptyException(
                    "O carrinho esta vazio"
            );
        }
    }

    /**
     * Valida se há estoque suficiente para todos os itens
     */
    private void validateProductStock(CartResponseDTO cart) {
        for (CartItemResponseDTO item : cart.getItems()) {
            Product product =
                    productService.findById(
                            item.getProductId()
                    );

            if (product.getStockQuantity() < item.getQuantity()) {
                log.warn("Estoque insuficiente no checkout - productId={}, stock={}, required={}",
                        product.getId(), product.getStockQuantity(), item.getQuantity());
                throw new InsufficientStockException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }
        }
    }

    /**
     * Constrói request de pagamento Mercado Pago
     */
    private PaymentCreateRequest buildPaymentRequest(
            User user,
            CartResponseDTO cart,
            CheckoutRequestDTO dto
    ) {
        PaymentPayerRequest payerRequest =
                PaymentPayerRequest.builder()
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .identification(
                                IdentificationRequest.builder()
                                        .type("CPF")
                                        .number(user.getCpf())
                                        .build()
                        )
                        .build();

        PaymentCreateRequest.PaymentCreateRequestBuilder requestBuilder =
                PaymentCreateRequest.builder()
                        .transactionAmount(cart.getTotal())
                        .description("Pedido Giselle Piercing")
                        .payer(payerRequest);

        String type =
                dto.getPaymentMethod().toUpperCase();

        switch (type) {

            case "PIX" -> requestBuilder.paymentMethodId("pix");

            case "BOLETO" -> requestBuilder.paymentMethodId("bolbradesco");

            case "CREDIT_CARD" -> {

                if (dto.getToken() == null
                        || dto.getInstallments() == null) {

                    log.warn("Dados de cartão ausentes para pagamento com cartão");
                    throw new PaymentProcessingException(
                            "Dados de cartao ausentes"
                    );
                }

                requestBuilder
                        .paymentMethodId(
                                dto.getPaymentMethodId()
                                        .toLowerCase()
                        )
                        .token(dto.getToken())
                        .installments(dto.getInstallments());
            }

            default -> {
                log.error("Método de pagamento inválido: {}", type);
                throw new PaymentProcessingException(
                        "Metodo indisponivel"
                );
            }
        }

        return requestBuilder.build();
    }

    /**
     * Processa pagamento com Mercado Pago
     */
    private Payment processPaymentWithMercadoPago(PaymentCreateRequest request) throws Exception {
        PaymentClient client =
                new PaymentClient();

        return client.create(request);
    }

    /**
     * Cria pedido a partir do carrinho
     */
    private Long createOrderFromCart(
            Long userId,
            CartResponseDTO cart,
            Payment payment
    ) {
        String paymentUrl = extractPaymentUrl(payment);

        Order order = new Order();

        order.setUserId(userId);
        order.setTotal(cart.getTotal());
        order.setPaymentMethod(payment.getPaymentMethodId());
        order.setMercadoPagoId(
                payment.getId().toString()
        );
        order.setPaymentUrl(paymentUrl);
        order.setStatus(OrderStatus.PENDING);

        Long orderId =
                orderRepository.createOrder(order);

        for (CartItemResponseDTO item : cart.getItems()) {

            orderRepository.createOrderItem(
                    orderId,
                    item
            );
        }

        return orderId;
    }

    /**
     * Extrai URL de pagamento de acordo com tipo de método
     */
    private String extractPaymentUrl(Payment payment) {
        String paymentUrl = null;
        String paymentMethod = payment.getPaymentMethodId();

        if ("pix".equals(paymentMethod)
                && payment.getPointOfInteraction() != null
                && payment.getPointOfInteraction()
                .getTransactionData() != null) {

            paymentUrl =
                    payment.getPointOfInteraction()
                            .getTransactionData()
                            .getQrCode();
        } else if ("bolbradesco".equals(paymentMethod)
                && payment.getTransactionDetails() != null) {

            paymentUrl =
                    payment.getTransactionDetails()
                            .getExternalResourceUrl();
        }

        return paymentUrl;
    }

    public void teste() {
        BigDecimal bigDecimal = new BigDecimal(12);
    }
}
