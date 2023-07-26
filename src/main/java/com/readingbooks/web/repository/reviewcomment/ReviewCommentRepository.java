package com.readingbooks.web.repository.reviewcomment;

import com.readingbooks.web.domain.entity.review.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query(
            "select rc " +
                    "from ReviewComment rc " +
                    "join fetch Member member " +
                    "join fetch Review review " +
                    "where rc.id = :reviewCommentId"
    )
    Optional<ReviewComment> findReviewComment(@Param("reviewCommentId") Long reviewCommentId);
}
