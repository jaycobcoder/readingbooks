package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.login.LoginCheckFailException;
import com.readingbooks.web.exception.login.NotLoginException;
import com.readingbooks.web.exception.member.MemberNotFoundException;
import com.readingbooks.web.exception.member.MemberPresentException;
import com.readingbooks.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(RegisterRequest request){
        String email = request.getEmail();
        validateIsExistsEmail(email);
        validateForm(request);

        Member member = Member.createMember(request);

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.encodePassword(encodedPassword);

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public void validateIsExistsEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if(findMember.isPresent()){
            throw new MemberPresentException("이미 가입된 이메일입니다.");
        }
    }

    private void validateForm(RegisterRequest request) {
        String email = request.getEmail();
        if(email == null){
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }

        String[] splitEmail = email.split("@");
        if(splitEmail[0].length() < 4 || splitEmail[0].length() > 24){
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }

        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();
        if(password == null || passwordConfirm == null){
            throw new IllegalArgumentException("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
        }

        if((password.length() < 8 || password.length() > 16) || (passwordConfirm.length() < 8 || password.length() > 16)){
            throw new IllegalArgumentException("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
        }

        if(!password.equals(passwordConfirm)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String name = request.getName();
        if(name == null){
            throw new IllegalArgumentException("이름을 올바르게 입력해주세요.");
        }
        if(name.length() > 20 || name.length() < 2){
            throw new IllegalArgumentException("이름을 올바르게 입력해주세요.");
        }

        String birthYear = request.getBirthYear();
        if(birthYear.length() != 4){
            throw new IllegalArgumentException("생년을 올바르게 입력해주세요.");
        }

        Gender gender = request.getGender();
        if(gender == null){
            throw new IllegalArgumentException("성별을 올바르게 입력해주세요.");
        }
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 이메일로 회원을 찾을 수 없습니다. 이메일을 다시 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public Member findMember(Principal principal) {
        if(principal == null){
            throw new NotLoginException("로그인 하셔야 이용할 수 있습니다.");
        }

        String email = principal.getName();
        return findMember(email);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디로 회원을 찾을 수 없습니다. 회원 아이디를 다시 확인해주세요."));
    }
}
