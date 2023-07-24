package com.readingbooks.web.repository.wishlist;

import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByBookIdAndMemberId(Long bookId, Long memberId);

    @Query(
            "select w " +
                    "from Wishlist w " +
                    "join w.member m " +
                    "join w.book b " +
                    "where m.id = :memberId"
    )
    List<Wishlist> findByMemberId(@Param("memberId") Long memberId);

    @Query(
            "select w " +
                    "from Wishlist w " +
                    "where w.member.id = :memberId and w.book.id in(:bookIdList)"
    )
    List<Wishlist> findByMemberIdAndBookIds(@Param("memberId") Long memberId, @Param("bookIdList") List<Long> bookIdList);
}
