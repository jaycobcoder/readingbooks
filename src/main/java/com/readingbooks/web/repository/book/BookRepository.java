package com.readingbooks.web.repository.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, SearchBookRepository, HomeRepository{
    boolean existsByCategoryId(Long categoryId);

    boolean existsByBookGroupId(Long bookGroupId);

    List<Book> findByTitle(String title);

    @Query(
            "select b from Book b " +
                    "join fetch b.category " +
                    "join fetch b.category.categoryGroup " +
                    "where b.isbn = :isbn"
    )
    Optional<Book> getBookInformation(@Param("isbn") String isbn);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByBookGroupId(Long bookGroupId);
}
