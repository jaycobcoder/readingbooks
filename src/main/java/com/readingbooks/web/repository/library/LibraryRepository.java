package com.readingbooks.web.repository.library;

import com.readingbooks.web.domain.entity.library.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    @Query(
            "select count(l.id) > 0 " +
                    "from Library l " +
                    "where l.book.id in(:bookIdList)"
    )
    boolean existsByBookIds(@Param("bookIdList") List<Long> bookIdList);

    boolean existsByBookId(Long bookId);
}
