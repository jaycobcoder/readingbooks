package com.readingbooks.web.domain.entity.author;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.enums.AuthorOption;
import jakarta.persistence.*;

@Entity
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthorOption authorOption;
}
