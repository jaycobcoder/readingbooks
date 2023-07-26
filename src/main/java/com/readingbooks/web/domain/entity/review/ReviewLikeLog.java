package com.readingbooks.web.domain.entity.review;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReviewLikeLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewLikeLog createReviewLikeLog(Member member, Review review) {
        ReviewLikeLog log = new ReviewLikeLog();
        log.member = member;
        log.review = review;
        return log;
    }
}