package com.gisellepiercing.repository;

import com.gisellepiercing.model.User;
import com.gisellepiercing.repository.query.UserQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper =
            (rs, rowNum) -> {

                User user = new User();

                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                return user;
            };

    public void save(User user) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole());

        jdbcTemplate.update(UserQuery.INSERT_USER, params);
    }

    public Optional<User> findByEmail(String email) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        List<User> users = jdbcTemplate.query(
                UserQuery.FIND_BY_EMAIL,
                params,
                userRowMapper
        );

        return users.stream().findFirst();
    }
}