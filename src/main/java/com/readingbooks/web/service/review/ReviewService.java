package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.exception.review.ReviewNotFoundException;
import com.readingbooks.web.exception.review.ReviewPresentException;
import com.readingbooks.web.repository.library.LibraryRepository;
import com.readingbooks.web.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LibraryRepository libraryRepository;

    /**
     * 리뷰 작성 메소드
     * @param member
     * @param book
     * @param content
     * @param starRating
     * @return reviewId
     */
    @Transactional
    public Long review(Member member, Book book, String content, int starRating){
        /* --- 폼 검증 --- */
        validateForm(content, starRating);

        /* --- 이미 해당 도서에 리뷰를 작성했는지 검증 --- */
        validateMemberWroteReviewInSameBook(member.getId());

        /* --- 리뷰 작성자가 리뷰를 작성했는지 확인 --- */
        boolean isPurchased = libraryRepository.existsByMemberId(member.getId());

        /* --- 리뷰 카운트 추가 --- */
        book.addReviewCount();

        Review review = Review.createReview(member, book, content,starRating, isPurchased);
        return reviewRepository.save(review).getId();
    }

    private void validateMemberWroteReviewInSameBook(Long memberId) {
        boolean result = reviewRepository.existsByMemberId(memberId);
        if(result == true){
            throw new ReviewPresentException("이미 해당 도서에 리뷰를 작성했습니다.");
        }
    }

    private void validateForm(String content, int starRating) {
        if(content == null || content.trim() == ""){
            throw new IllegalArgumentException("리뷰를 남겨주세요.");
        }

        if(content.length() < 10){
            throw new IllegalArgumentException("10자 이상의 리뷰를 남겨주세요.");
        }

        if(content.length() > 2000){
            throw new IllegalArgumentException("2000자 미만의 리뷰를 남겨주세요.");
        }

        if (starRating > 5 || starRating < 1) {
            throw new IllegalArgumentException("별점은 1에서 5의 정수만 올 수 있습니다.");
        }
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰 아이디로 리뷰를 찾을 수 없습니다."));
    }
}
