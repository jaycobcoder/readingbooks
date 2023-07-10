package com.readingbooks.web.service.manage.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookUpdateRequest {
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private Long categoryId;
    private Long bookGroupId;
}