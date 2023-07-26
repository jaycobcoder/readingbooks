package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.review.Review;
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
    private List<ReviewCommentResponse> reviewComments = new ArrayList<>();

    public ReviewResponse(Review review) {
        this.memberId = review.getMember().getId();
        this.bookId = review.getBook().getId();
        this.reviewId = review.getId();
        this.maskedId = createMaskedId(review.getMember().getEmail());
        this.isPurchased = review.isPurchased();
        this.content = review.getContent();

        if(review.getLikesCount() > 999){
            this.likesCount = "999+";
        }else{
            this.likesCount = String.valueOf(review.getLikesCount());
        }

        if(review.getCommentsCount() > 999){
            this.commentsCount = "999+";
        }else{
            this.commentsCount = String.valueOf(review.getCommentsCount());
        }

        if(review.getReviewComments().size() > 0){
            this.reviewComments = review.getReviewComments().stream()
                    .map(rc -> new ReviewCommentResponse(rc))
                    .collect(Collectors.toList());
        }
    }

    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
