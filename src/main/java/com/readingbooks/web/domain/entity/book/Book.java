package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.service.manage.book.BookRegisterRequest;
import com.readingbooks.web.service.manage.book.BookUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private int salePrice;
    private String savedImageName;
    private boolean isOnSale;
    private int reviewCount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_group_id")
    private BookGroup bookGroup;

    @OneToMany(mappedBy = "book")
    private List<BookAuthorList> bookAuthorLists = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();

    public static Book createBook(BookRegisterRequest request, Category category, BookGroup bookGroup, String savedImageName){
        Book book = new Book();
        book.title = request.getTitle();
        book.isbn = request.getIsbn();
        book.publisher = request.getPublisher();
        book.publishingDate = request.getPublishingDate();
        book.paperPrice = request.getPaperPrice();
        book.ebookPrice = request.getEbookPrice();
        book.discountRate = request.getDiscountRate();

        int discountPrice = (int) (request.getEbookPrice() * request.getDiscountRate() * 0.01);
        book.salePrice = request.getEbookPrice() - discountPrice;

        book.category = category;
        book.bookGroup = bookGroup;
        book.savedImageName = savedImageName;
        book.description = request.getDescription();
        book.isOnSale = true;
        book.reviewCount = 0;
        return book;
    }

    public void update(String imageName) {
        savedImageName = imageName;
    }

    public void update(BookUpdateRequest request, Category category, BookGroup bookGroup) {
        this.title = request.getTitle();
        this.isbn = request.getIsbn();
        this.publisher = request.getPublisher();
        this.publishingDate = request.getPublishingDate();
        this.paperPrice = request.getPaperPrice();
        this.ebookPrice = request.getEbookPrice();
        this.discountRate = request.getDiscountRate();
        this.category = category;
        this.bookGroup = bookGroup;
        this.description = request.getDescription();

        int discountPrice = (int) (request.getEbookPrice() * request.getDiscountRate() * 0.01);
        this.salePrice = request.getEbookPrice() - discountPrice;
    }

    public void addReviewCount(){
        reviewCount++;
    }

    public void subtractReviewCount(){
        reviewCount--;
    }
}
