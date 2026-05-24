package com.gisellepiercing.repository.query;

public class OrderQuery {

    private OrderQuery() {
    }

    public static final String CREATE_ORDER = """
        INSERT INTO store.orders
        (
            user_id,
            total,
            status
        ) VALUES (
            :userId,
            :total,
            :status
        )
        RETURNING id
    """;

    public static final String CREATE_ORDER_ITEM = """
        INSERT INTO store.order_items
        (
            order_id,
            product_id,
            product_name,
            price,
            quantity
        ) VALUES (
            :orderId,
            :productId,
            :productName,
            :price,
            :quantity
        )
    """;

    public static final String CLEAR_CART = """
        DELETE FROM store.cart_items
        WHERE cart_id = :cartId
    """;

    public static final String UPDATE_ORDER_STATUS = """
        UPDATE store.orders
        SET status = :status
        WHERE id = :orderId
    """;
}