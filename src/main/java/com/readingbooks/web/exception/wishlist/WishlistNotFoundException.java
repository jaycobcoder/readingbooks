package com.readingbooks.web.exception.wishlist;

import com.readingbooks.web.exception.base.PresentException;

public class WishlistNotFoundException extends PresentException {
    public WishlistNotFoundException(String message) {
        super(message);
    }
}
