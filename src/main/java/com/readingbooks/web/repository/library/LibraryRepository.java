package com.readingbooks.web.repository.library;

import com.readingbooks.web.domain.entity.library.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    @Query(
            "select count(l.id) > 0 " +
                    "from Library l " +
                    "where l.book.id in(:bookIdList)"
    )
    boolean existsByBookIds(@Param("bookIdList") List<Long> bookIdList);

    boolean existsByBookId(Long bookId);

    boolean existsByMemberId(Long id);

    @Query(
            "select l " +
                    "from Library l " +
                    "join fetch l.book " +
                    "where l.member.id = :memberId"
    )
    List<Library> findAllByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);
}
