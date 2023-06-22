package com.readingbooks.web.domain.entity.category;

import com.readingbooks.web.domain.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class CategoryGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_group_id")
    private Long id;
    private String name;
}
