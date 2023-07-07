package com.readingbooks.web.exception.category;

import com.readingbooks.web.exception.base.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
