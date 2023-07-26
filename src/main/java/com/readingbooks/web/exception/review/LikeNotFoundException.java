package com.readingbooks.web.exception.review;

import com.readingbooks.web.exception.base.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(String message) {
        super(message);
    }
}
