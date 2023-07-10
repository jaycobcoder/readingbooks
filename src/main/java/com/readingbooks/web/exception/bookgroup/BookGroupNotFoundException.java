package com.readingbooks.web.exception.bookgroup;

import com.readingbooks.web.exception.base.NotFoundException;

public class BookGroupNotFoundException extends NotFoundException {

    public BookGroupNotFoundException(String message) {
        super(message);
    }
}
