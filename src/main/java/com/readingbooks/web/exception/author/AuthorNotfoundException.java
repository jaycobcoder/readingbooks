package com.readingbooks.web.exception.author;

import com.readingbooks.web.exception.base.NotFoundException;

public class AuthorNotfoundException extends NotFoundException {
    public AuthorNotfoundException(String message) {
        super(message);
    }
}
