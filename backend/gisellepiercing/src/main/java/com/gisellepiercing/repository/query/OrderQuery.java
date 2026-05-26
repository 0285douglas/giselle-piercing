package com.gisellepiercing.repository.query;

public class OrderQuery {

    private OrderQuery() {
    }

    public static final String CREATE_ORDER = """
        INSERT INTO store.orders
        (
            user_id,
            total,
            payment_method,
            mercado_pago_id,
            payment_url,
            status
        ) VALUES (
            :userId,
            :total,
            :paymentMethod,
            :mercadoPagoId,
            :paymentUrl,
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

    public static final String FIND_ITEMS_BY_ORDER_ID = """
        SELECT
            id,
            order_id,
            product_id,
            product_name,
            price,
            quantity
        FROM store.order_items
        WHERE order_id = :orderId
    """;

    public static final String FIND_USER_ID_BY_ORDER_ID = """
        SELECT user_id
        FROM store.orders
        WHERE id = :orderId
    """;
}