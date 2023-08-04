package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.member.MemberPresentException;
import com.readingbooks.web.repository.member.MemberRepository;
import com.readingbooks.web.service.mail.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceMockTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MailService mailService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("필드가 null일 시 IllegalArgumentException이 잘 터지는지")
    void whenRegisteringFormNull_thenThrowException(){
        // given
        RegisterRequest request = createRequest(null, null, null, null, null, null, null);

        // when
        Exception exception = assertThrows(Exception.class,
                () -> memberService.register(request));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비밀번호가 서로 틀렸을 때")
    void whenRegisteringNotMatchedPasswords_thenThrowException(){
        // given
        RegisterRequest request = createRequest("test@example.com", "test1234", "helloworld1234", "test1234", "1999", Gender.SECRET, "01012341234");

        // when
        Exception exception = assertThrows(Exception.class,
                () -> memberService.register(request));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 가입된 이메일로 회원가입을 요청할 때")
    void whenRegisteringExistedEmail_thenThrowException(){
        //given
        String email = "test@example.com";
        RegisterRequest request = createRequest(email, "test1234!", "test1234!", "test", "1999", Gender.SECRET, "01012341234");

        given(memberRepository.existsByEmail(email))
                .willReturn(true);

        //when
        Exception exception = assertThrows(Exception.class,
                () -> memberService.register(request));

        //then
        assertThat(exception).isInstanceOf(MemberPresentException.class);
        assertThat(exception.getMessage()).isEqualTo("이미 가입된 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 성공")
    void whenMemberRegistered_thenVerifyFields(){
        // given
        Member member = Member.createMember(getSuccessRequest());

        Long memberId = 1L;
        ReflectionTestUtils.setField(member, "id", memberId);

        doReturn(member)
                .when(memberRepository)
                .save(any(Member.class));
        doReturn(Optional.ofNullable(member))
                .when(memberRepository)
                .findById(any(Long.class));

        // when
        Long registeredMemberId = memberService.register(getSuccessRequest());

        // then
        Member findMember = memberRepository.findById(registeredMemberId).get();

        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(member.getName()).isEqualTo(findMember.getName());
        assertThat(member.getBirthYear()).isEqualTo(findMember.getBirthYear());
        assertThat(member.getGender()).isEqualTo(findMember.getGender());
        assertThat(member.getPhoneNo()).isEqualTo(findMember.getPhoneNo());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("비밀번호 변경 시 메소드 호출")
    void whenChangedTempPassword_thenVerifyPassword(){
        //given
        String email = "test@gmail.com";
        String phoneNo = "01055555555";

        Member member = Member.createMember(
                createRequest(email, "test1234", "test1234",
                        "홍길동", "1999", Gender.MEN, phoneNo)
        );
        
        doReturn(Optional.ofNullable(member))
                .when(memberRepository)
                .findByEmailAndPhoneNo(any(String.class), any(String.class));
        doReturn(Optional.ofNullable(member))
                .when(memberRepository)
                .findByEmail(any(String.class));

        //when
        memberService.changePasswordAndSendEmail(email, phoneNo);
        memberService.findMember(email);

        //then
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(mailService, times(1)).send(any(String.class), any(String.class));
    }

    private RegisterRequest getSuccessRequest() {
        return createRequest("test@gmail.com", "test1234", "test1234", "홍길동", "1999", Gender.MEN, "01055555555");
    }

    private RegisterRequest createRequest(String email, String password, String passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new RegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }
}
