package com.readingbooks.web.repository.book;

import com.readingbooks.web.domain.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
