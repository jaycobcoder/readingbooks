package com.readingbooks.web.exception.reviewcomment;

import com.readingbooks.web.exception.base.PresentException;

public class ReviewCommentNotFoundException extends PresentException {
    public ReviewCommentNotFoundException(String message) {
        super(message);
    }
}
