package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.author.Author;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookAuthorList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_author_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    private int ordinal;

    public static BookAuthorList createBookAuthorList(Book book, Author author, int ordinal) {
        BookAuthorList bookAuthorList = new BookAuthorList();
        bookAuthorList.book = book;
        bookAuthorList.author = author;
        bookAuthorList.ordinal = ordinal;
        return bookAuthorList;
    }
}
