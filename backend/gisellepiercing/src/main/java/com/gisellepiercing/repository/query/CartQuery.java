package com.gisellepiercing.repository.query;

public class CartQuery {

    private CartQuery() {
    }

    public static final String CREATE_CART = """
        INSERT INTO store.cart (user_id)
        VALUES (:userId)
    """;

    public static final String FIND_CART_BY_USER_ID = """
        SELECT id, user_id
        FROM store.cart
        WHERE user_id = :userId
    """;

    public static final String ADD_ITEM = """
        INSERT INTO store.cart_items
        (
            cart_id,
            product_id,
            quantity
        )
        VALUES
        (
            :cartId,
            :productId,
            :quantity
        )
    """;

    public static final String FIND_ITEMS_BY_CART_ID = """
        SELECT
            id,
            cart_id,
            product_id,
            quantity
        FROM store.cart_items
        WHERE cart_id = :cartId
    """;

    public static final String DELETE_ITEM = """
        DELETE FROM store.cart_items
        WHERE id = :id
    """;

    public static final String FIND_CART_ITEMS_DETAILS = """
        SELECT
            ci.id AS item_id,
            ci.product_id,
            ci.quantity,
            p.name AS product_name,
            p.image_url,
            p.price
        FROM store.cart_items ci
        INNER JOIN store.products p
            ON p.id = ci.product_id
        WHERE ci.cart_id = :cartId
    """;

    public static final String CLEAR_CART_ITEMS = """
        DELETE FROM store.cart_items
        WHERE cart_id = :cartId
    """;
}
