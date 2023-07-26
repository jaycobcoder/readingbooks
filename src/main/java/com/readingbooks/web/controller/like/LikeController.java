package com.readingbooks.web.controller.like;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.like.LikeService;
import com.readingbooks.web.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
@Slf4j
public class LikeController {

    private final LikeService likeService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Object> like(Principal principal, Long reviewId){
        Member member = memberService.findMember(principal);

        likeService.like(member, reviewId);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "좋아요 처리 되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
