package com.readingbooks.web.service.reviewcomment;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.domain.entity.review.ReviewComment;
import com.readingbooks.web.exception.review.ReviewException;
import com.readingbooks.web.exception.reviewcomment.ReviewCommentNotFoundException;
import com.readingbooks.web.repository.reviewcomment.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    /**
     * 리뷰 댓글 삭제 메소드
     * @param member
     * @param reviewCommentId
     * @return isDeleted
     */
    public boolean delete(Member member, Long reviewCommentId){
        ReviewComment reviewComment = findReview(reviewCommentId);

        /* --- 본인이 작성한 댓글인지 확인 --- */
        validateCommentIdentification(reviewComment, member.getId());

        /* --- 댓글 수량 차감 --- */
        Review review = reviewComment.getReview();
        review.subtractCommentsCount();

        reviewCommentRepository.delete(reviewComment);
        return true;
    }

    @Transactional(readOnly = true)
    public ReviewComment findReview(Long commentId){
        return reviewCommentRepository.findReviewComment(commentId)
                .orElseThrow(() -> new ReviewCommentNotFoundException("댓글을 찾을 수 없습니다. 댓글 아이디를 다시 확인해주세요."));
    }

    private void validateCommentIdentification(ReviewComment reviewComment, Long memberId) {
        Long findMemberId = reviewComment.getMember().getId();
        if(findMemberId != memberId){
            throw new ReviewException("본인이 작성한 리뷰만 관리할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
