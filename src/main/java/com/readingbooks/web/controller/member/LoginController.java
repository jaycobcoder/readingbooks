package com.readingbooks.web.controller.member;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.member.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@ModelAttribute RegisterRequest request){

        memberService.register(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "회원가입 되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
