package com.gisellepiercing.service;

import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.model.Order;
import com.gisellepiercing.model.OrderStatus;
import com.gisellepiercing.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckoutService {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public CheckoutService(CartService cartService, ProductService productService, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public Long checkout(Long userId) {
        CartResponseDTO cart = cartService.getCart(userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(cart.getTotal());
        order.setStatus(OrderStatus.PENDING);

        Long orderId = orderRepository.createOrder(order);

        for (CartItemResponseDTO item : cart.getItems()) {
            orderRepository.createOrderItem(orderId, item);
            productService.decreaseStock(item.getProductId(), item.getQuantity());
        }

        orderRepository.clearCart(cart.getCartId());
        log.info("Checkout completed orderId={} userId={}", orderId, userId);
        return orderId;
    }
}