package com.readingbooks.web.controller.review;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.review.MyWroteReviewResponse;
import com.readingbooks.web.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class ReviewViewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping
    public String review(Principal principal, Model model){
        Member member = memberService.findMember(principal);
        List<MyWroteReviewResponse> responses = reviewService.findWroteReviews(member.getId());

        model.addAttribute("responses", responses);
        model.addAttribute("active", "review");
        return "user/review/review";
    }
}
