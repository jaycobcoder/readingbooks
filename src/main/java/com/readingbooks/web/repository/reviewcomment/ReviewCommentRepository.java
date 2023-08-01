package com.readingbooks.web.repository.reviewcomment;

import com.readingbooks.web.domain.entity.review.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    List<ReviewComment> findAllByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewComment rc where rc.id in :reviewCommentIds")
    void deleteAllByIdInQuery(@Param("reviewCommentIds") List<Long> reviewCommentIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewComment rc where rc.review.id in :reviewIds")
    void deleteAllByReviewIdInQuery(@Param("reviewIds") List<Long> reviewIds);
}
