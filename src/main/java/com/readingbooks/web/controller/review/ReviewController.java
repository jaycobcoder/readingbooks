package com.readingbooks.web.controller.review;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Object> review(Principal principal, Long bookId, String content, Double starRating){
        Member member = memberService.findMember(principal);
        Book book = bookService.findBook(bookId);
        int intStarRating = starRating.intValue();
        reviewService.review(member, book, content, intStarRating);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "리뷰가 작성되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }

    @PatchMapping
    public ResponseEntity<Object> update(Principal principal, Long reviewId, String content, Double starRating){
        Member member = memberService.findMember(principal);
        int intStarRating = starRating.intValue();

        reviewService.update(member, reviewId, content, intStarRating);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK, "리뷰가 수정되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(baseResponse);
    }
}
