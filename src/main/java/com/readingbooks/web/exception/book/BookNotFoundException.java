package com.readingbooks.web.exception.book;

import com.readingbooks.web.exception.base.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
