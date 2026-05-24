package com.gisellepiercing.repository.query;

public class UserQuery {

    public static final String INSERT_USER = """
        INSERT INTO store.users (
            first_name,
            last_name,
            email,
            password,
            cpf,
            role
        ) VALUES (
            :firstName,
            :lastName,
            :email,
            :password,
            :cpf,
            :role
        )
    """;

    public static final String FIND_BY_EMAIL = """
        SELECT
            id,
            first_name,
            last_name,
            email,
            password,
            cpf,
            role
        FROM store.users
        WHERE email = :email
    """;

    public static final String EXISTS_BY_EMAIL = """
        SELECT COUNT(*)
        FROM store.users
        WHERE email = :email
    """;

    public static final String EXISTS_BY_CPF = """
        SELECT COUNT(*)
        FROM store.users
        WHERE cpf = :cpf
    """;
}