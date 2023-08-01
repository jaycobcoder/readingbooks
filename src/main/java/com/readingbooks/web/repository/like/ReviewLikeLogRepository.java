package com.readingbooks.web.repository.like;

import com.readingbooks.web.domain.entity.review.ReviewLikeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeLogRepository extends JpaRepository<ReviewLikeLog, Long> {
    boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);

    Optional<ReviewLikeLog> findByMemberIdAndReviewId(Long memberId, Long reviewId);

    List<ReviewLikeLog> findAllByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewLikeLog l where l.id in :reviewLikeLogIds")
    void deleteAllByIdInQuery(@Param("reviewLikeLogIds") List<Long> reviewLikeLogIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewLikeLog l where l.review.id in :reviewIds")
    void deleteAllByReviewIdInQuery(@Param("reviewIds") List<Long> reviewIds);
}
