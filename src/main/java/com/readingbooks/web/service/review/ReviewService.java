package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.exception.base.NotFoundException;
import com.readingbooks.web.exception.review.ReviewException;
import com.readingbooks.web.exception.review.ReviewNotFoundException;
import com.readingbooks.web.exception.review.ReviewPresentException;
import com.readingbooks.web.repository.library.LibraryRepository;
import com.readingbooks.web.repository.review.ReviewRepository;
import com.readingbooks.web.service.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LibraryRepository libraryRepository;
    private final BookService bookService;

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
        validateMemberWroteReviewInSameBook(member.getId(), book.getId());

        /* --- 리뷰 작성자가 리뷰를 작성했는지 확인 --- */
        boolean isPurchased = libraryRepository.existsByMemberId(member.getId());

        /* --- 리뷰 카운트 추가 --- */
        book.addReviewCount();

        Review review = Review.createReview(member, book, content,starRating, isPurchased);
        return reviewRepository.save(review).getId();
    }

    private void validateMemberWroteReviewInSameBook(Long memberId, Long bookId) {
        boolean result = reviewRepository.existsByMemberIdAndBookId(memberId, bookId);
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
        return reviewRepository.findReview(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰 아이디로 리뷰를 찾을 수 없습니다."));
    }

    public Review findReview(Long memberId, Long bookId){
        return reviewRepository.findByMemberIdAndBookId(memberId, bookId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다. 회원 아이디와 도서 아이디를 확인해주세요."));
    }

    /**
     * 도서에서 내가 작성한 리뷰 반환해주는 메소드
     *
     * @param memberId
     * @param isbn
     * @return MyWroteReviewResponse DTO
     */
    public MyWroteReviewResponse findWroteReview(Long memberId, String isbn) {
        Book book = bookService.findBook(isbn);
        Long bookId = book.getId();

        Review review = null;
        try{
            review = findReview(memberId, bookId);
        } catch (NotFoundException e){
            return null;
        }
        MyWroteReviewResponse response = new MyWroteReviewResponse(review);
        return response;
    }

    /**
     * 리뷰 수정 메소드
     * @param member
     * @param reviewId
     * @param content
     * @param starRating
     */

    @Transactional
    public void update(Member member, Long reviewId, String content, int starRating) {
        Long memberId = member.getId();
        Review review = findReview(reviewId);

        /* --- 수정하고자 하는 리뷰가 본인이 작성한 리뷰인지 검증 --- */
        validateReviewIdentification(review, memberId);

        /* --- 폼 검증 --- */
        validateForm(content, starRating);

        review.update(content, starRating);
    }

    private void validateReviewIdentification(Review review, Long memberId) {
        Long findMemberId = review.getMember().getId();
        if(findMemberId != memberId){
            throw new ReviewException("본인이 작성한 리뷰만 관리할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 리뷰 삭제 메소드
     * @param member
     * @param reviewId
     */
    @Transactional
    public boolean delete(Member member, Long reviewId) {
        Review review = findReview(reviewId);
        Long memberId = member.getId();

        /* --- 삭제하고자 하는 리뷰가 본인이 작성한 리뷰인지 검증 --- */
        validateReviewIdentification(review, memberId);

        /* --- 도서의 리뷰 수량 차감 --- */
        Book book = review.getBook();
        book.subtractReviewCount();

        reviewRepository.delete(review);
        return true;
    }

    public List<ReviewResponse> findReviews(String isbn){
        Book book = bookService.findBook(isbn);
        Long bookId = book.getId();

        List<Review> reviews = reviewRepository.findReviews(bookId);

        return reviews.stream()
                .map(r -> new ReviewResponse(r))
                .collect(Collectors.toList());
    }
}
