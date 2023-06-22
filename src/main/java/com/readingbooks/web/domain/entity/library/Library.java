package com.readingbooks.web.domain.entity.library;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.domain.entity.member.Member;
import jakarta.persistence.*;

@Entity
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
    private BookContent bookContent;
}
