package com.readingbooks.web.repository.bookcontent;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookContentRepository extends JpaRepository<BookContent, Book> {
    Optional<BookContent> findByBookId(Long bookId);

    boolean existsByBookId(Long bookId);

    @Query(
            "select bc " +
                    "from BookContent bc " +
                    "join fetch bc.book " +
                    "where bc.book.id = :bookId"
    )
    BookContent findBookContent(@Param("bookId") Long bookId);
}
