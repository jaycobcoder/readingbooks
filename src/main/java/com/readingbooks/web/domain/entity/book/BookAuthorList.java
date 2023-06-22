package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class BookAuthorList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    private int ordinal;
}
