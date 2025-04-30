package com.astrolink.AstroLink.exception.custom;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}