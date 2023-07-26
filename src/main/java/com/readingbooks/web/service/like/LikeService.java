package com.readingbooks.web.service.like;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.domain.entity.review.ReviewLikeLog;
import com.readingbooks.web.exception.like.LikePresentException;
import com.readingbooks.web.exception.review.LikeNotFoundException;
import com.readingbooks.web.repository.like.ReviewLikeLogRepository;
import com.readingbooks.web.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {

    private final ReviewLikeLogRepository reviewLikeLogRepository;
    private final ReviewService reviewService;

    /**
     * 좋아요 메소드
     * @param member
     * @param reviewId
     */
    public void like(Member member, Long reviewId) {
        Long memberId = member.getId();
        Review review = reviewService.findReview(reviewId);

        /* --- 이미 좋아요를 눌렀는지 확인 --- */
        boolean result = reviewLikeLogRepository.existsByReviewIdAndMemberId(reviewId, memberId);

        /* --- 좋아요 토글 --- */
        if(result == true){
            ReviewLikeLog log = reviewLikeLogRepository.findByMemberIdAndReviewId(memberId, reviewId)
                    .orElseThrow(() -> new LikeNotFoundException("좋아요를 조회할 수 없습니다. 회원 아이디와 리뷰 아이디를 확인해주세요."));
            review.subtractLikesCount();
            reviewLikeLogRepository.delete(log);
        } else{
            ReviewLikeLog log = ReviewLikeLog.createReviewLikeLog(member, review);
            review.addLikesCount();
            reviewLikeLogRepository.save(log);
        }
    }
}
