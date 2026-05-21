package com.gisellepiercing.repository.query;

public class ProductQuery {

    private ProductQuery() {
    }

    public static final String BASE_FIND_PRODUCTS = """
        SELECT
            p.id,
            p.name,
            p.description,
            p.price,
            p.image_url,
            p.stock_quantity,
            p.minimum_stock,
            c.name AS category,
            m.name AS material
        FROM store.products p
        INNER JOIN store.categories c
            ON p.category_id = c.id
        INNER JOIN store.materials m
            ON p.material_id = m.id
        WHERE 1 = 1
    """;

    public static final String FILTER_CATEGORY = """
        AND LOWER(c.name) = LOWER(:category)
    """;

    public static final String FILTER_MATERIAL = """
        AND LOWER(m.name) = LOWER(:material)
    """;

    public static final String ORDER_BY = """
        ORDER BY p.id
    """;

    public static final String INSERT_PRODUCT = """
        INSERT INTO store.products
        (
            name,
            description,
            price,
            image_url,
            category_id,
            material_id,
            stock_quantity,
            minimum_stock
        )
        VALUES
        (
            :name,
            :description,
            :price,
            :imageUrl,
            :categoryId,
            :materialId,
            :stockQuantity,
            :minimumStock
        )
    """;

    public static final String FIND_BY_ID = """
        SELECT
            p.id,
            p.name,
            p.description,
            p.price,
            p.image_url,
            p.stock_quantity,
            p.minimum_stock,
            c.name AS category,
            m.name AS material
        FROM store.products p
        INNER JOIN store.categories c
            ON c.id = p.category_id
        INNER JOIN store.materials m
            ON m.id = p.material_id
        WHERE p.id = :id
    """;

    public static final String UPDATE_PRODUCT = """
        UPDATE store.products
        SET
            name = :name,
            description = :description,
            price = :price,
            image_url = :imageUrl,
            category_id = :categoryId,
            material_id = :materialId,
            stock_quantity = :stockQuantity,
            minimum_stock = :minimumStock
        WHERE id = :id
    """;

    public static final String DELETE_PRODUCT = """
        DELETE FROM store.products
        WHERE id = :id
    """;

    public static final String DECREASE_STOCK = """
        UPDATE store.products
        SET stock_quantity = stock_quantity - :quantity
        WHERE id = :productId
    """;
}