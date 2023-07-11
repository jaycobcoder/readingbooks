package com.readingbooks.web.repository.bookcontent;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookContentRepository extends JpaRepository<BookContent, Book> {
    Optional<BookContent> findByBookId(Long bookId);
}
