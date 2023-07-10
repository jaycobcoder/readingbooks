package com.readingbooks.web.repository.bookgroup;

import com.readingbooks.web.domain.entity.book.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGroupRepository extends JpaRepository<BookGroup, Long> {
}
