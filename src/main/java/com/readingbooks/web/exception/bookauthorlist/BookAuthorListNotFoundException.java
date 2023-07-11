package com.readingbooks.web.exception.bookauthorlist;

import com.readingbooks.web.exception.base.NotFoundException;

public class BookAuthorListNotFoundException extends NotFoundException {
    public BookAuthorListNotFoundException(String message) {
        super(message);
    }
}
