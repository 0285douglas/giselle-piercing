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
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCpf(rs.getString("cpf"));
                user.setRole(rs.getString("role"));
                return user;
            };

    public void save(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("cpf", user.getCpf())
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

    // Novo método necessário para o AuthService validar e-mails duplicados
    public boolean existsByEmail(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        Integer count = jdbcTemplate.queryForObject(UserQuery.EXISTS_BY_EMAIL, params, Integer.class);
        return count != null && count > 0;
    }

    public boolean existsByCpf(String cpf) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpf", cpf);

        Integer count = jdbcTemplate.queryForObject(UserQuery.EXISTS_BY_CPF, params, Integer.class);
        return count != null && count > 0;
    }
}