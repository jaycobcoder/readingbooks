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
                    "where l.book.id in(:bookIdList) and l.member.id = :memberId"
    )
    boolean existsByBookIdsAndMemberId(@Param("bookIdList") List<Long> bookIdList, @Param("memberId") Long memberId);

    boolean existsByBookIdAndMemberId(Long bookId, Long memberId);

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
