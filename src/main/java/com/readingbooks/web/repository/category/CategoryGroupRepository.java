package com.readingbooks.web.repository.category;

import com.readingbooks.web.domain.entity.category.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
}
