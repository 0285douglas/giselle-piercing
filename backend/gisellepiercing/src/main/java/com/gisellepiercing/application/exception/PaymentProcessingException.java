package com.gisellepiercing.application.exception;

/**
 * Exceção lançada quando há erro durante processamento de pagamento (integração Mercado Pago)
 */
public class PaymentProcessingException extends RuntimeException {

    public PaymentProcessingException(String message) {
        super(message);
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

