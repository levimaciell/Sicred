package com.ufpb.sicred.exceptions;

public class InvalidUserCreationException extends RuntimeException {
    public InvalidUserCreationException(String msg) {
        super(msg);
    }
}
