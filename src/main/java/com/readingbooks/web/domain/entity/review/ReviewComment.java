package com.readingbooks.web.domain.entity.review;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReviewComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
    private Long id;

    @Column(length = 2000)
    private String content;
    private boolean isHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewComment createReviewComment(Member member, Review review, String content) {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.member = member;
        reviewComment.review = review;
        reviewComment.content = content;
        reviewComment.isHidden = false;
        return reviewComment;
    }
}