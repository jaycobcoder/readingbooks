package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.review.ReviewComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ReviewCommentResponse {
    private String email;
    private Long reviewId;
    private Long reviewCommentId;
    private String maskedId;
    private String content;
    private String wroteDate;

    public ReviewCommentResponse(ReviewComment reviewComment) {
        email = reviewComment.getMember().getEmail();
        reviewId = reviewComment.getReview().getId();
        reviewCommentId = reviewComment.getId();
        maskedId = createMaskedId(reviewComment.getMember().getEmail());
        content = reviewComment.getContent();
        wroteDate = createWroteDate(reviewComment.getCreatedTime());
    }

    private String createWroteDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        int hour = currentDate.toLocalTime().getHour();
        int minute = currentDate.getMinute();
        return year + "." + month + "." + day + ". " + hour + ":" + minute;
    }

    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
