package com.gisellepiercing.repository;

import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.model.Order;
import com.gisellepiercing.repository.query.OrderQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrder(Order order) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", order.getUserId())
                .addValue("total", order.getTotal())
                .addValue("status", order.getStatus().name());

        return jdbcTemplate.queryForObject(OrderQuery.CREATE_ORDER, params, Long.class);
    }

    public void createOrderItem(Long orderId, CartItemResponseDTO item) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderId", orderId)
                .addValue("productId", item.getProductId())
                .addValue("productName", item.getProductName())
                .addValue("price", item.getPrice())
                .addValue("quantity", item.getQuantity());

        jdbcTemplate.update(OrderQuery.CREATE_ORDER_ITEM, params);
    }

    public void clearCart(Long cartId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cartId", cartId);

        jdbcTemplate.update(OrderQuery.CLEAR_CART, params);
    }
}