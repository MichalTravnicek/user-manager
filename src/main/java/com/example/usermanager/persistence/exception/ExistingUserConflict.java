package com.example.usermanager.persistence.exception;

public class ExistingUserConflict extends RuntimeException{

    public ExistingUserConflict(final String message) {
        super(message);
    }
}
