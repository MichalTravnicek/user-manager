package com.example.usermanager.persistence.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(final Exception ex) {
        super(ex);
    }
}
