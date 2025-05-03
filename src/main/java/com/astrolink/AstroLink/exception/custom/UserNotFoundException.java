package com.astrolink.AstroLink.exception.custom;

public class UserNotFoundException extends DataNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}