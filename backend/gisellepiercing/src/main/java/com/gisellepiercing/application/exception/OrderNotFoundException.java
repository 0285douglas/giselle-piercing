package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando um pedido não é encontrado no sistema
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

