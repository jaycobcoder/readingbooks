package com.readingbooks.web.controller.review;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.review.ReviewService;
import com.readingbooks.web.service.reviewcomment.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review-comment")
@Slf4j
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Object> comment(Principal principal, Long reviewId, String content){
        Member member = memberService.findMember(principal);
        Review review = reviewService.findReview(reviewId);

        reviewCommentService.comment(member, review, content);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "댓글이 작성되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }

    @DeleteMapping("/{reviewCommentId}")
    public ResponseEntity<Object> delete(Principal principal, @PathVariable Long reviewCommentId){
        Member member = memberService.findMember(principal);

        reviewCommentService.delete(member, reviewCommentId);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "댓글이 삭제되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }
}
