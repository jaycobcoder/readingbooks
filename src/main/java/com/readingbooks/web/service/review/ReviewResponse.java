package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.domain.entity.review.ReviewComment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewResponse {
    private Long memberId;
    private Long bookId;
    private Long reviewId;
    private String maskedId;
    private boolean isPurchased;
    private String content;
    private String likesCount;
    private String commentsCount;
    private int starRating;
    private List<ReviewCommentResponse> reviewComments;

    public ReviewResponse(Review review) {
        this.memberId = review.getMember().getId();
        this.bookId = review.getBook().getId();
        this.reviewId = review.getId();
        this.maskedId = createMaskedId(review.getMember().getEmail());
        this.isPurchased = review.isPurchased();
        this.content = review.getContent();
        this.likesCount = createLikesCount(review.getLikesCount());
        this.commentsCount = createCommentsCount(review.getCommentsCount());
        this.starRating = review.getStarRating();

        List<ReviewComment> reviewComments = review.getReviewComments();
        this.reviewComments = reviewComments.stream()
                .map(rc -> new ReviewCommentResponse(rc))
                .collect(Collectors.toList());
    }

    private String createCommentsCount(int commentsCount) {
        if(commentsCount > 999){
            return "999+";
        }else{
            return String.valueOf(commentsCount);
        }
    }

    private String createLikesCount(int reviewLikesCount) {
        if(reviewLikesCount > 999){
            return "999+";
        }else{
            return String.valueOf(reviewLikesCount);
        }
    }

    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
