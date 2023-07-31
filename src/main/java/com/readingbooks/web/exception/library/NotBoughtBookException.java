package com.readingbooks.web.exception.library;

public class NotBoughtBookException extends RuntimeException {
    public NotBoughtBookException(String message) {
        super(message);
    }
}
