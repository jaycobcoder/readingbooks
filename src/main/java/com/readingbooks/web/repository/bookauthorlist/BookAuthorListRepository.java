package com.readingbooks.web.repository.bookauthorlist;

import com.readingbooks.web.domain.entity.book.BookAuthorList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookAuthorListRepository extends JpaRepository<BookAuthorList, Long> {

    Optional<BookAuthorList> findByBookIdAndAuthorId(Long bookId, Long authorId);

    boolean existsByAuthorId(Long authorId);
}
