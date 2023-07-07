package com.readingbooks.web.repository.admin.author;

import com.readingbooks.web.domain.entity.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
