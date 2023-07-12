package com.readingbooks.web.repository.category;

import com.readingbooks.web.domain.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByCategoryGroupId(Long categoryGroupId);

    @Query("select c " +
            "from Category c " +
            "join fetch c.categoryGroup " +
            "where c.name = :name")
    Optional<Category> findByName(@Param("name") String name);
}
