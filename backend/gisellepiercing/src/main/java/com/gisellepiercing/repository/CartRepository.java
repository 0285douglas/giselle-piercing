package com.gisellepiercing.repository;

import com.gisellepiercing.dto.cart.Cart;
import com.gisellepiercing.dto.cart.CartItem;
import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.repository.query.CartQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CartRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cart> cartRowMapper = (rs, rowNum) -> {
        Cart cart = new Cart();
        cart.setId(rs.getLong("id"));
        cart.setUserId(rs.getLong("user_id"));
        return cart;
    };

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setCartId(rs.getLong("cart_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    };

    public Cart createCart(Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        jdbcTemplate.update(CartQuery.CREATE_CART, params);
        return findCartByUserId(userId).orElseThrow();
    }

    public Optional<Cart> findCartByUserId(Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<Cart> carts = jdbcTemplate.query(
                CartQuery.FIND_CART_BY_USER_ID,
                params,
                cartRowMapper
        );

        return carts.stream().findFirst();
    }

    public void addItem(Long cartId, Long productId, Integer quantity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cartId", cartId)
                .addValue("productId", productId)
                .addValue("quantity", quantity);

        jdbcTemplate.update(CartQuery.ADD_ITEM, params);
    }

    public List<CartItem> findItemsByCartId(Long cartId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cartId", cartId);

        return jdbcTemplate.query(
                CartQuery.FIND_ITEMS_BY_CART_ID,
                params,
                cartItemRowMapper
        );
    }

    public void deleteItem(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(CartQuery.DELETE_ITEM, params);
    }

    public List<CartItemResponseDTO> findDetailedItems(Long cartId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cartId", cartId);

        return jdbcTemplate.query(
                CartQuery.FIND_CART_ITEMS_DETAILS,
                params,
                (rs, rowNum) -> {
                    BigDecimal price = rs.getBigDecimal("price");
                    Integer quantity = rs.getInt("quantity");

                    return CartItemResponseDTO.builder()
                            .itemId(rs.getLong("item_id"))
                            .productId(rs.getLong("product_id"))
                            .productName(rs.getString("product_name"))
                            .imageUrl(rs.getString("image_url"))
                            .price(price)
                            .quantity(quantity)
                            .subtotal(price.multiply(BigDecimal.valueOf(quantity)))
                            .build();
                }
        );
    }

    public void clearCart(Long cartId) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cartId", cartId);

        jdbcTemplate.update(
                CartQuery.CLEAR_CART_ITEMS,
                params
        );
    }
}