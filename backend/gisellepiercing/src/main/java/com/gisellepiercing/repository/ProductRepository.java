package com.gisellepiercing.repository;

import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.query.ProductQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> productRowMapper =
            (rs, rowNum) -> {

                Product product = new Product();

                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));
                product.setCategory(rs.getString("category"));
                product.setMaterial(rs.getString("material"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setMinimumStock(rs.getInt("minimum_stock"));

                return product;
            };

    public List<Product> findProducts(String category, String material) {
        StringBuilder sql = new StringBuilder(ProductQuery.BASE_FIND_PRODUCTS);
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (category != null && !category.isBlank()) {
            sql.append(ProductQuery.FILTER_CATEGORY);
            params.addValue("category", category);
        }

        if (material != null && !material.isBlank()) {
            sql.append(ProductQuery.FILTER_MATERIAL);
            params.addValue("material", material);
        }

        sql.append(ProductQuery.ORDER_BY);
        return jdbcTemplate.query(sql.toString(), params, productRowMapper);
    }

    public Product save(Product product) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("name", product.getName())
                        .addValue("description", product.getDescription())
                        .addValue("price", product.getPrice())
                        .addValue("imageUrl", product.getImageUrl())
                        .addValue("categoryId", Long.parseLong(product.getCategory()))
                        .addValue("materialId", Long.parseLong(product.getMaterial()))
                        .addValue("stockQuantity", product.getStockQuantity())
                        .addValue("minimumStock", product.getMinimumStock());

        jdbcTemplate.update(ProductQuery.INSERT_PRODUCT, params);
        return product;
    }

    public Optional<Product> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("id", id);

        List<Product> products = jdbcTemplate.query(ProductQuery.FIND_BY_ID, params, productRowMapper);
        return products.stream().findFirst();
    }

    public Product update(Long id, Product product) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("name", product.getName())
                        .addValue("description", product.getDescription())
                        .addValue("price", product.getPrice())
                        .addValue("imageUrl", product.getImageUrl())
                        .addValue("categoryId", Long.parseLong(product.getCategory()))
                        .addValue("materialId", Long.parseLong(product.getMaterial()))
                        .addValue("stockQuantity", product.getStockQuantity())
                        .addValue("minimumStock", product.getMinimumStock());

        jdbcTemplate.update(ProductQuery.UPDATE_PRODUCT, params);
        product.setId(id);
        return product;
    }

    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("id", id);

        jdbcTemplate.update(ProductQuery.DELETE_PRODUCT, params);
    }

    public void decreaseStock(Long productId, Integer quantity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("productId", productId)
                .addValue("quantity", quantity);

        jdbcTemplate.update(ProductQuery.DECREASE_STOCK, params);
    }
}