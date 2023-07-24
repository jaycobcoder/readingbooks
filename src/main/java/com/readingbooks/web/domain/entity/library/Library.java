package com.readingbooks.web.domain.entity.library;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Library extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public static Library createLibrary(Member member, Book book) {
        Library library = new Library();
        library.member = member;
        library.book = book;
        return library;
    }
}
