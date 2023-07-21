package com.readingbooks.web.repository.bookauthorlist;

import com.readingbooks.web.domain.entity.book.BookAuthorList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookAuthorListRepository extends JpaRepository<BookAuthorList, Long> {

    Optional<BookAuthorList> findByBookIdAndAuthorId(Long bookId, Long authorId);

    boolean existsByAuthorId(Long authorId);

    boolean existsByBookId(Long bookId);

    @Query("select bal " +
            "from BookAuthorList bal " +
            "join bal.book b " +
            "join bal.author a " +
            "where b.isbn = :isbn and a.authorOption = com.readingbooks.web.domain.enums.AuthorOption.AUTHOR " +
            "order by bal.ordinal asc " +
            "limit 1")
    BookAuthorList getMainAuthor(@Param("isbn") String isbn);

    @Query("select count(bal) " +
            "from BookAuthorList bal " +
            "join bal.book b " +
            "where bal.book.isbn = :isbn ")
    Long getAuthorCount(@Param("isbn") String isbn);

    @Query("select bal " +
            "from BookAuthorList bal " +
            "join bal.book " +
            "join bal.author " +
            "where bal.book.isbn = :isbn and bal.author.authorOption = com.readingbooks.web.domain.enums.AuthorOption.TRANSLATOR " +
            "order by bal.ordinal asc " +
            "limit 1")
    BookAuthorList getMainTranslator(@Param("isbn") String isbn);

    @Query(
            "select bal " +
                    "from BookAuthorList bal " +
                    "join bal.author a " +
                    "join bal.book b " +
                    "where b.isbn = :isbn"
    )
    List<BookAuthorList> getAuthorNameAndIdList(@Param("isbn") String isbn);

    @Query(
            "select bal " +
                    "from BookAuthorList bal " +
                    "join fetch bal.author a " +
                    "join bal.book b " +
                    "where b.isbn = :isbn and a.id = :authorId"
    )
    Optional<BookAuthorList> getAuthorInformation(@Param("isbn") String isbn, @Param("authorId") Long authorId);
}
