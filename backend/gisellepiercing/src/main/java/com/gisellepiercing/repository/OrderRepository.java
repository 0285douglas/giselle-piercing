package com.gisellepiercing.repository;

import com.gisellepiercing.dto.response.CartItemResponseDTO;
import com.gisellepiercing.model.Order;
import com.gisellepiercing.model.OrderItem;
import com.gisellepiercing.repository.query.OrderQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .addValue("paymentMethod", order.getPaymentMethod())
                .addValue("mercadoPagoId", order.getMercadoPagoId())
                .addValue("paymentUrl", order.getPaymentUrl())
                .addValue("status", order.getStatus().name());

        return jdbcTemplate.queryForObject(
                OrderQuery.CREATE_ORDER,
                params,
                Long.class
        );
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

    public void updateStatus(Long orderId, String status) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderId", orderId)
                .addValue("status", status);

        jdbcTemplate.update(OrderQuery.UPDATE_ORDER_STATUS, params);
    }

    public List<OrderItem> findItemsByOrderId(Long orderId) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        return jdbcTemplate.query(
                OrderQuery.FIND_ITEMS_BY_ORDER_ID,
                params,
                orderItemRowMapper
        );
    }

    public Long findUserIdByOrderId(Long orderId) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        return jdbcTemplate.queryForObject(
                OrderQuery.FIND_USER_ID_BY_ORDER_ID,
                params,
                Long.class
        );
    }

    private final RowMapper<OrderItem> orderItemRowMapper =
            (rs, rowNum) -> {

                OrderItem item = new OrderItem();

                item.setId(rs.getLong("id"));
                item.setOrderId(rs.getLong("order_id"));
                item.setProductId(rs.getLong("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setQuantity(rs.getInt("quantity"));

                return item;
            };
}