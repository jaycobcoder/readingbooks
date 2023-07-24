package com.readingbooks.web.service.order;

import com.readingbooks.web.domain.entity.book.Book;
import lombok.Getter;

@Getter
public class OrderBooksResponse {
    private String title;
    private String isbn;

    public OrderBooksResponse(Book book) {
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
    }
}
