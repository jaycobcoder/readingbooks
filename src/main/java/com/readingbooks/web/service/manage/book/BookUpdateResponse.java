package com.readingbooks.web.service.manage.book;

import com.readingbooks.web.domain.entity.book.Book;
import lombok.Getter;

@Getter
public class BookUpdateResponse {
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private String savedImageName;
    private Long categoryId;
    private Long bookGroupId;
    private String description;

    public BookUpdateResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.publisher = book.getPublisher();
        this.publishingDate = book.getPublishingDate();
        this.paperPrice = book.getPaperPrice();
        this.ebookPrice = book.getEbookPrice();
        this.discountRate = book.getDiscountRate();
        this.savedImageName = book.getSavedImageName();
        this.categoryId = book.getCategory().getId();
        if(book.getBookGroup() == null){
            this.bookGroupId = null;
        }else{
            this.bookGroupId = book.getBookGroup().getId();
        }
        this.description = book.getDescription();
    }
}
