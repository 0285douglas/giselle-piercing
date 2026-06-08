package com.gisellepiercing.service;

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

@Service
@Slf4j
public class CheckoutService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    public CheckoutService(
            UserRepository userRepository,
            OrderRepository orderRepository,
            CartService cartService,
            ProductService productService
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    @Transactional
    public Payment processarCheckout(
            Long userId,
            String userEmail,
            CheckoutRequestDTO dto
    ) {

        log.info(
                "Processando checkout de metodo={} para o usuarioId={}",
                dto.getPaymentMethod(),
                userId
        );

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario nao encontrado"
                        )
                );

        CartResponseDTO cart =
                cartService.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "O carrinho esta vazio"
            );
        }

        for (CartItemResponseDTO item : cart.getItems()) {

            Product product =
                    productService.findById(
                            item.getProductId()
                    );

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }
        }

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

            case "PIX" ->
                    requestBuilder.paymentMethodId("pix");

            case "BOLETO" ->
                    requestBuilder.paymentMethodId("bolbradesco");

            case "CREDIT_CARD" -> {

                if (dto.getToken() == null
                        || dto.getInstallments() == null) {

                    throw new IllegalArgumentException(
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

            default ->
                    throw new IllegalArgumentException(
                            "Metodo indisponivel"
                    );
        }

        try {

            PaymentClient client =
                    new PaymentClient();

            Payment payment =
                    client.create(requestBuilder.build());

            String paymentUrl = null;

            if (type.equals("PIX")
                    && payment.getPointOfInteraction() != null
                    && payment.getPointOfInteraction()
                    .getTransactionData() != null) {

                paymentUrl =
                        payment.getPointOfInteraction()
                                .getTransactionData()
                                .getQrCode();
            }

            else if (type.equals("BOLETO")
                    && payment.getTransactionDetails() != null) {

                paymentUrl =
                        payment.getTransactionDetails()
                                .getExternalResourceUrl();
            }

            Order order = new Order();

            order.setUserId(userId);
            order.setTotal(cart.getTotal());
            order.setPaymentMethod(type);
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

            log.info(
                    "Pedido gerado id={} com sucesso",
                    orderId
            );

            return payment;

        } catch (Exception e) {

            log.error(
                    "Erro ao integrar com o Mercado Pago",
                    e
            );

            throw new RuntimeException(
                    "Falha na operacao de pagamento: "
                            + e.getMessage()
            );
        }
    }
}