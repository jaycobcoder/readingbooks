package com.readingbooks.web.service.manage.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
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
    private String description;

    public BookUpdateRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishingDate = publishingDate;
        this.paperPrice = paperPrice;
        this.ebookPrice = ebookPrice;
        this.discountRate = discountRate;
        this.categoryId = categoryId;
        if(bookGroupId == 0){
            this.bookGroupId = null;
        } else{
            this.bookGroupId = bookGroupId;
        }
        this.description = description;
    }
}