package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando dados do produto são inválidos ou produto não existe
 */
public class ProductInvalidException extends RuntimeException {

    public ProductInvalidException(String message) {
        super(message);
    }

    public ProductInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

