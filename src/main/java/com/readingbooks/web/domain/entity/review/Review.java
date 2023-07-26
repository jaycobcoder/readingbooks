package com.readingbooks.web.domain.entity.review;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(length = 2000)
    private String content;
    private Integer starRating;
    private boolean isPurchased;
    private boolean isHidden;
    private int likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Review createReview(Member member, Book book, String content, int starRating, boolean isPurchased) {
        Review review = new Review();
        review.member = member;
        review.book = book;
        review.content = content;
        review.starRating = starRating;
        review.likesCount = 0;
        review.isHidden = false;
        review.isPurchased = isPurchased;
        return review;
    }

    public void update(String content, int starRating) {
        this.content = content;
        this.starRating = starRating;
    }
}
