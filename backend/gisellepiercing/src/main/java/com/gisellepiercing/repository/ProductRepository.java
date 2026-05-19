package com.gisellepiercing.repository;

import com.gisellepiercing.model.Product;
import com.gisellepiercing.repository.query.ProductQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        Long categoryId = getCategoryIdByName(product.getCategory());

        Long materialId = getMaterialIdByName(product.getMaterial());

        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("name", product.getName())
                        .addValue("description", product.getDescription())
                        .addValue("price", product.getPrice())
                        .addValue("imageUrl", product.getImageUrl())
                        .addValue("categoryId", categoryId)
                        .addValue("materialId", materialId);

        jdbcTemplate.update(ProductQuery.INSERT_PRODUCT, params);

        return product;
    }

    private Long getCategoryIdByName(String category) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("category", category);

        return jdbcTemplate.queryForObject(ProductQuery.FIND_CATEGORY_ID_BY_NAME, params, Long.class);
    }

    private Long getMaterialIdByName(String material) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("material", material);

        return jdbcTemplate.queryForObject(ProductQuery.FIND_MATERIAL_ID_BY_NAME, params, Long.class);
    }
}