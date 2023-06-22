package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.category.Category;
import jakarta.persistence.*;

@Entity
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_group_id")
    private BookGroup bookGroup;
}
