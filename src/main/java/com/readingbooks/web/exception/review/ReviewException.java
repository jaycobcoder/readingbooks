package com.readingbooks.web.exception.review;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReviewException extends RuntimeException {
    private HttpStatus status;
    public ReviewException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
