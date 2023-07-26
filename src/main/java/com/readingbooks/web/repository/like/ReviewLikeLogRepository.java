package com.readingbooks.web.repository.like;

import com.readingbooks.web.domain.entity.review.ReviewLikeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeLogRepository extends JpaRepository<ReviewLikeLog, Long> {
    boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);

    Optional<ReviewLikeLog> findByMemberIdAndReviewId(Long memberId, Long reviewId);
}
