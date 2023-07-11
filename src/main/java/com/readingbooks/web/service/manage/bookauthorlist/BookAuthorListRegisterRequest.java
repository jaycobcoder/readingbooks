package com.readingbooks.web.service.manage.bookauthorlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookAuthorListRegisterRequest {
    private Long bookId;
    private Long authorId;
    private int ordinal;
}
