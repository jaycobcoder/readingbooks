package com.readingbooks.web.service.home;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HomeBooksResponse {
    private String isbn;
    private String savedImageName;
    private String title;
    private String author;
    private int authorCountExceptMainAuthor;
    private String reviewRatingAvg;
    private int reviewCount;

    @QueryProjection
    public HomeBooksResponse(String isbn, String savedImageName, String title,
                             String author, Long authorCount, int totalStarRating,
                             int reviewCount) {
        this.isbn = isbn;
        this.savedImageName = savedImageName;
        this.title = title;
        this.author = author;
        if(authorCount == null){
            this.authorCountExceptMainAuthor = 0;
        }else{
            this.authorCountExceptMainAuthor = authorCount.intValue() - 1;
        }
        this.reviewRatingAvg = calculateReviewAvg(totalStarRating, reviewCount);
        this.reviewCount = reviewCount;
    }

    private String calculateReviewAvg(Integer totalStarRating, int reviewCount) {
        if(totalStarRating == 0 || totalStarRating == null){
            return "0";
        }
        double avg = (double) totalStarRating / reviewCount;
        return String.format("%.1f", avg);
    }
}
