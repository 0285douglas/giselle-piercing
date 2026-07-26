package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando não há quantidade suficiente em estoque para realizar operação
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}

