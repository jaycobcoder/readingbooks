package com.readingbooks.web.repository.reviewcomment;

import com.readingbooks.web.domain.entity.review.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
}
