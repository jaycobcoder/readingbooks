package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.login.NotLoginException;
import com.readingbooks.web.exception.member.MemberException;
import com.readingbooks.web.exception.member.MemberNotFoundException;
import com.readingbooks.web.exception.member.MemberPresentException;
import com.readingbooks.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        String phoneNo = request.getPhoneNo();
        if(phoneNo == null || phoneNo.trim().equals("")){
            throw new IllegalArgumentException("핸드폰 번호를 올바르게 입력해주세요.");
        }

        if(phoneNo.length() != 11){
            throw new IllegalArgumentException("핸드폰 번호를 올바르게 입력해주세요.");
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

    /**
     * 비밀번호 확인 이후 DTO 반환 메소드
     * @param rawPassword
     * @param member
     * @return dto
     */
    public ModifyMemberResponse matchPasswordThenReturnResponse(String rawPassword, Member member) {
        String encodedPassword = member.getPassword();

        matchPasswords(rawPassword, encodedPassword);

        return new ModifyMemberResponse(member);
    }

    private void matchPasswords(String rawPassword, String encodedPassword) {
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);

        if(result == false){
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 비밀번호 업데이트
     * @param password
     * @param newPassword
     * @param newPasswordConfirm
     * @param member
     */
    @Transactional
    public void update(String password, String newPassword, String newPasswordConfirm, Member member) {
        String encodedPassword = member.getPassword();

        validatePasswords(password, newPassword, newPasswordConfirm);

        matchPasswords(password, encodedPassword);

        String changingPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(changingPassword);
    }

    private void validatePasswords(String password, String newPassword, String newPasswordConfirm) {
        if(password == null || password.trim().equals("")){
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if(newPassword == null || newPassword.trim().equals("")){
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if(newPasswordConfirm == null || newPasswordConfirm.trim().equals("")){
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if(!newPassword.equals(newPasswordConfirm)){
            throw new IllegalArgumentException("변경할 비밀번호가 일치하지 않습니다.");
        }
    }

    public MemberInformationResponse findMemberInformation(Principal principal) {
        Member member = findMember(principal);
        return new MemberInformationResponse(member);
    }

    public List<String> findEmail(String name, String phoneNo) {
        List<Member> members = memberRepository.findByNameAndPhoneNo(name, phoneNo);

        if(members.size() == 0){
            throw new MemberNotFoundException("이름과 핸드폰 번호에 해당하는 아이디를 찾을 수 없습니다.");
        }

        return members.stream().map(m -> maskEmail(m.getEmail()))
                .collect(Collectors.toList());
    }

    public String maskEmail(String email){
        int atIndex = email.indexOf('@');

        String prefix = email.substring(0, 3);
        String domain = email.substring(atIndex);
        String maskedPrefix = prefix + "*".repeat(atIndex - 3); // 빼기 3을 한 이유는, 앞 3글자는 마스킹 되지 않아야 함
        return maskedPrefix + domain;
    }
}
