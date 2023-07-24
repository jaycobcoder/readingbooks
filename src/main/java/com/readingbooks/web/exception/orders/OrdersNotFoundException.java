package com.readingbooks.web.exception.orders;

import com.readingbooks.web.exception.base.NotFoundException;

public class OrdersNotFoundException extends NotFoundException {
    public OrdersNotFoundException(String message) {
        super(message);
    }
}
