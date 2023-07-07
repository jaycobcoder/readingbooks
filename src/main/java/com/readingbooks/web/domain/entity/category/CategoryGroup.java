package com.readingbooks.web.domain.entity.category;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.service.manage.category.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryGroupUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CategoryGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_group_id")
    private Long id;
    private String name;

    public static CategoryGroup createCategoryGroup(CategoryGroupRegisterRequest request) {
        CategoryGroup categoryGroup = new CategoryGroup();
        categoryGroup.name = request.getName();
        return categoryGroup;
    }

    public void updateCategoryGroup(CategoryGroupUpdateRequest request) {
        name = request.getName();
    }
}