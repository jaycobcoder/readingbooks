package com.readingbooks.web.repository.bookgroup;

import com.readingbooks.web.domain.entity.book.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookGroupRepository extends JpaRepository<BookGroup, Long> {
    List<BookGroup> findByTitle(String title);
}
