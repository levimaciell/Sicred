package com.ufpb.sicred.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
