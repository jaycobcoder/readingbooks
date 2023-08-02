package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.review.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class MyWroteReviewResponse {
    private Long reviewId;
    private Long bookId;
    private String title;
    private String isbn;
    private String content;
    private int starRating;
    private String wroteDate;

    public MyWroteReviewResponse(Review review) {
        reviewId = review.getId();
        bookId = review.getBook().getId();
        title = review.getBook().getTitle();
        isbn = review.getBook().getIsbn();
        content = review.getContent();
        starRating = review.getStarRating();
        wroteDate = createWroteDate(review.getCreatedTime());
    }

    private String createWroteDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        return year + "." + month + "." + day + ".";
    }
}
