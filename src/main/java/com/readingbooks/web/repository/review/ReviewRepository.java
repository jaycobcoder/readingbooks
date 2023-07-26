package com.readingbooks.web.repository.review;

import com.readingbooks.web.domain.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMemberIdAndBookId(Long memberId, Long bookId);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);
}
