package com.readingbooks.web.repository.category;

import com.readingbooks.web.domain.entity.category.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    boolean existsByName(String name);

    Optional<CategoryGroup> findByName(String name);
}
