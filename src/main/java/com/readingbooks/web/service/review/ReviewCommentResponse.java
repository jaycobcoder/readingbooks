package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.review.ReviewComment;
import lombok.Getter;

@Getter
public class ReviewCommentResponse {
    private Long memberId;
    private Long reviewId;
    private Long reviewCommentId;
    private String maskedId;
    private String content;

    public ReviewCommentResponse(ReviewComment reviewComment) {
        memberId = reviewComment.getMember().getId();
        reviewId = reviewComment.getReview().getId();
       reviewCommentId = reviewComment.getId();
       maskedId = createMaskedId(reviewComment.getMember().getEmail());
       content = reviewComment.getContent();
    }

    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
