package com.readingbooks.web.service.mail;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.repository.member.MemberRepository;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.member.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class MailServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MailService mailService = mock(MailService.class);

    private MemberService memberService;

    @BeforeEach
    void beforeEach(){
        memberService = new MemberService(memberRepository, passwordEncoder, mailService);
    }

    @Test
    void whenFoundPassword_thenVerifyEmailSending(){
        //given
        RegisterRequest request = new RegisterRequest("test@gmail.com", "testtest1234", "testtest1234", "홍길동", "1999", Gender.SECRET, "01055555555");
        memberService.register(request);

        //when
        String email = "test@gmail.com";
        String tempPassword = memberService.changePasswordAndSendEmail(email, "01055555555");

        //then
        verify(mailService).send(email, tempPassword);
    }
}