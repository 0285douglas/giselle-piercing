package com.gisellepiercing.service;

import com.gisellepiercing.dto.cart.Cart;
import com.gisellepiercing.dto.cart.CartItem;
import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class CartService {

    private final CartRepository repository;

    public CartService(CartRepository repository) {
        this.repository = repository;
    }

    public Cart getOrCreateCart(Long userId) {
        return repository.findCartByUserId(userId)
                .orElseGet(() -> {
                    log.info("Creating cart for userId={}", userId);
                    return repository.createCart(userId);
                });
    }

    public void addItem(Long userId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        repository.addItem(cart.getId(), productId, quantity);
        log.info("Item added to cart userId={} productId={} quantity={}", userId, productId, quantity);
    }

    public CartResponseDTO getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItemResponseDTO> items = repository.findDetailedItems(cart.getId());

        BigDecimal total = items.stream()
                .map(CartItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .userId(userId)
                .items(items)
                .total(total)
                .build();
    }

    public void removeItem(Long itemId) {
        repository.deleteItem(itemId);
        log.info("Cart item removed id={}", itemId);
    }
}