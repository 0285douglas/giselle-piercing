package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando tenta fazer checkout com carrinho vazio
 */
public class CartEmptyException extends RuntimeException {

    public CartEmptyException(String message) {
        super(message);
    }

    public CartEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}

