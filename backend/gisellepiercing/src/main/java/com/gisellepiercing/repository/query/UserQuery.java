package com.gisellepiercing.repository.query;

public class UserQuery {

    public static final String INSERT_USER = """
        INSERT INTO store.users(
            name,
            email,
            password,
            role
        ) VALUES (
            :name,
            :email,
            :password,
            :role)
    """;

    public static final String FIND_BY_EMAIL = """
        SELECT
            id,
            name,
            email,
            password,
            role
        FROM store.users
        WHERE email = :email
    """;
}