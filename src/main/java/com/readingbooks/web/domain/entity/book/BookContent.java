package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class BookContent extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    private String content;
}
