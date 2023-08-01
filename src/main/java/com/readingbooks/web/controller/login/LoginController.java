package com.readingbooks.web.controller.login;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.member.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

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

    @PostMapping("/register/validate/email")
    public ResponseEntity<Object> validateEmail(@RequestParam String email){
        memberService.validateIsExistsEmail(email);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "사용 가능한 이메일입니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/account/find-id")
    public ResponseEntity<Object> findId(@RequestBody HashMap<String, String> request){
        String name = request.get("name");
        String phoneNo = request.get("phoneNo");
        List<String> maskedEmail = memberService.findEmail(name, phoneNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(maskedEmail);
    }

    @PostMapping("/account/find-password")
    public ResponseEntity<Object> findPassword(@RequestParam String email, @RequestParam String phoneNo){
        memberService.changePasswordAndSendEmail(email, phoneNo);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "비밀번호가 변경되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/account/leave")
    public ResponseEntity<Object> leave(Principal principal, String password, HttpServletRequest request){
        Member member = memberService.findMember(principal);

        memberService.leave(member, password);

        /* --- 세션 로그인을 구현한 상태. 로그인 -> 로그아웃으로 변경 --- */
        HttpSession session = request.getSession();
        session.invalidate();

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "회원탈퇴 처리되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}