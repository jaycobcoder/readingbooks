package com.readingbooks.web.service.reviewcomment;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.domain.entity.review.ReviewComment;
import com.readingbooks.web.repository.reviewcomment.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;

    /**
     * 댓글 작성 메소드
     * @param member
     * @param review
     * @param content
     */
    public Long comment(Member member, Review review, String content) {
        validateForm(content);
        
        /* --- 댓글 작성 --- */
        ReviewComment reviewComment = ReviewComment.createReviewComment(member, review, content);
        
        /* --- 댓글 수량 증가 --- */
        review.addCommentsCount();
        return reviewCommentRepository.save(reviewComment).getId();
    }

    private void validateForm(String content) {
        if(content == null || content.trim().equals("")){
            throw new IllegalArgumentException("댓글을 입력해주세요");
        }

        if(content.length() > 2000){
            throw new IllegalArgumentException("2000자 미만의 댓글을 남겨주세요.");
        }
    }
}
