package com.readingbooks.web.service.library;

import com.readingbooks.web.domain.entity.book.BookContent;
import lombok.Getter;

@Getter
public class BookContentResponse {
    private String title;
    private String content;
    private String isbn;

    public BookContentResponse(BookContent bookContent) {
        title = bookContent.getBook().getTitle();
        content = bookContent.getContent();
        isbn = bookContent.getBook().getIsbn();
    }
}
