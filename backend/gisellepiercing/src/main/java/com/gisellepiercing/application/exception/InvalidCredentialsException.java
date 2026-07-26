package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando credentials (email ou senha) são inválidas durante login
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}

