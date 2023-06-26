package com.readingbooks.web.exception.member;

import com.readingbooks.web.exception.base.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
