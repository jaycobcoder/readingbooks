package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.service.manage.book.BookRegisterRequest;
import com.readingbooks.web.service.manage.book.BookUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private String savedImageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_group_id")
    private BookGroup bookGroup;

    public static Book createBook(BookRegisterRequest request, Category category, BookGroup bookGroup, String savedImageName){
        Book book = new Book();
        book.title = request.getTitle();
        book.isbn = request.getIsbn();
        book.publisher = request.getPublisher();
        book.publishingDate = request.getPublishingDate();
        book.paperPrice = request.getPaperPrice();
        book.ebookPrice = request.getEbookPrice();
        book.discountRate = request.getDiscountRate();
        book.category = category;
        book.bookGroup = bookGroup;
        book.savedImageName = savedImageName;
        return book;
    }

    public void updateImage(String updatedImageName) {
        savedImageName = updatedImageName;
    }

    public void updateContent(BookUpdateRequest request, Category category, BookGroup bookGroup) {
        this.title = request.getTitle();
        this.isbn = request.getIsbn();
        this.publisher = request.getPublisher();
        this.publishingDate = request.getPublishingDate();
        this.paperPrice = request.getPaperPrice();
        this.ebookPrice = request.getEbookPrice();
        this.discountRate = request.getDiscountRate();
        this.category = category;
        this.bookGroup = bookGroup;
    }
}
