package com.readingbooks.web.service.manage.bookcontent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookContentUpdateResponse {
    private String bookTitle;
    private Long bookId;
    private String content;
}
