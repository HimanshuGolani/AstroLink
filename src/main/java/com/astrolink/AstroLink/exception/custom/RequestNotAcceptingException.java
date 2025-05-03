package com.astrolink.AstroLink.exception.custom;

public class RequestNotAcceptingException extends RuntimeException {
    public RequestNotAcceptingException(String message) {
        super(message);
    }
}