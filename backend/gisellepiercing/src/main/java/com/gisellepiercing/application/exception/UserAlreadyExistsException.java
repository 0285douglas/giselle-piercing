package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando um usuário tenta se registrar com email ou CPF já existentes
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

