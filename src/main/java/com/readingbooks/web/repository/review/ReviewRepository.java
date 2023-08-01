package com.readingbooks.web.repository.review;

import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.service.review.MyWroteReviewInBookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMemberIdAndBookId(Long memberId, Long bookId);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Query(
            "select r " +
                    "from Review r " +
                    "join r.member " +
                    "join r.book " +
                    "where r.id = :reviewId"
    )
    Optional<Review> findReview(@Param("reviewId") Long reviewId);

    @Query(
            "select distinct r " +
                    "from Review r " +
                    "join fetch r.member " +
                    "join fetch r.book " +
                    "left join fetch r.reviewComments rc " +
                    "where r.book.id = :bookId and r.isHidden = false and (rc.isHidden = false or rc is null) " +
                    "order by r.likesCount desc, r.createdTime asc "
    )
    List<Review> findReviews(@Param("bookId") Long bookId);

    @Query(
            "select r from Review r " +
                    "join fetch r.book " +
                    "join r.member " +
                    "where r.member.id = :memberId " +
                    "order by r.createdTime desc "
    )
    List<Review> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Review r where r.id in :reviewIds")
    void deleteAllByIdInQuery(@Param("reviewIds") List<Long> reviewIds);

    int countByCreatedTimeBetween(LocalDateTime startOfToday, LocalDateTime endOfToday);
}
