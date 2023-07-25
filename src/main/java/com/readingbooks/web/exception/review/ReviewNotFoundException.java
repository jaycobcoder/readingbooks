package com.readingbooks.web.exception.review;

import com.readingbooks.web.exception.base.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
