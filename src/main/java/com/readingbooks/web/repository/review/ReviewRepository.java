package com.readingbooks.web.repository.review;

import com.readingbooks.web.domain.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMemberId(Long memberId);
}
