package com.readingbooks.web.domain.entity.category;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.service.manage.category.CategoryUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;

    public static Category createCategory(String name, CategoryGroup categoryGroup) {
        Category category = new Category();
        category.name = name;
        category.categoryGroup = categoryGroup;
        return category;
    }

    public void updateCategory(CategoryUpdateRequest request) {
        this.name = request.getName();
    }
}