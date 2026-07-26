package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.InsufficientStockException;
import com.gisellepiercing.application.exception.ProductInvalidException;
import com.gisellepiercing.dto.cart.Cart;
import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.dto.response.CartResponseDTO;
import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class CartService {

    private final CartRepository repository;
    private final ProductService productService;

    public CartService(CartRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    public Cart getOrCreateCart(Long userId) {

        return repository.findCartByUserId(userId)
                .orElseGet(() -> {
                    log.info("Criando carrinho para userId={}", userId);
                    return repository.createCart(userId);
                });
    }

    public void addItem(Long userId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            log.warn("Tentativa de adicionar quantidade inválida ao carrinho: userId={}, productId={}, quantity={}", userId, productId, quantity);
            throw new ProductInvalidException("Quantidade deve ser maior que zero");
        }

        Product product = productService.findById(productId);

        if (product.getStockQuantity() < quantity) {
            log.warn("Estoque insuficiente: productId={}, stockAvailable={}, quantity={}", productId, product.getStockQuantity(), quantity);
            throw new InsufficientStockException("Estoque insuficiente para o produto: " + product.getName());
        }

        Cart cart = getOrCreateCart(userId);
        repository.addItem(cart.getId(), productId, quantity);
        log.info("Item adicionado ao carrinho com sucesso - userId={} productId={} quantity={}", userId, productId, quantity);
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
        log.info("Item id={} removido do carrinho ", itemId);
    }

    public void clearCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        repository.clearCart(cart.getId());
        log.info("Carrinho limpo userId={}", userId);
    }
}