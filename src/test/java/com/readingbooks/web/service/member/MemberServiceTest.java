package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.member.MemberPresentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    void register_fail_null(){
        RegisterRequest request = createRequest(null, null, null, null, null, null);
        assertThatThrownBy(
                ()-> memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void register_fail_invalid_email(){
        RegisterRequest request = createRequest("tes@example.com", "test1234", "test", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일을 올바르게 입력해주세요.");

    }

    @Test
    void register_fail_invalid_short_password(){
        RegisterRequest request = createRequest("test@example.com", "test", "test", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_not_contained_password(){
        RegisterRequest request = createRequest("test@example.com", "test", "test1234", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_name(){
        RegisterRequest request = createRequest("test@example.com", "test1234", "t", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_birthYear(){
        RegisterRequest request = createRequest("test@example.com", "test1234!", "test", "19990115", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_phoneNo(){
        RegisterRequest request = createRequest("test@example.com", "test1234!", "test", "1999", "0101234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸드폰 번호를 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_gender(){
        RegisterRequest request = createRequest("test@example.com", "test1234!", "test", "1999", "01012341234", null);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("성별을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_present_email(){
        //given
        RegisterRequest registerMember = createRequest("test@example.com", "test1234!", "test", "1999", "01012341234", Gender.SECRET);
        memberService.register(registerMember);

        RegisterRequest request = createRequest("test@example.com", "test1234", "test", "1999", "01012341234", Gender.MEN);

        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(MemberPresentException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
        void register_success(){
        //given
        RegisterRequest registerMember = createRequest("success@example.com", "test1234!", "test", "1999", "01012341234", Gender.SECRET);

        //when
        Long memberId = memberService.register(registerMember);

        //then
        assertThat(memberId).isEqualTo(1L);
    }

    private RegisterRequest createRequest(String email, String password, String name, String birthYear, String phoneNo, Gender gender){
        return new RegisterRequest(email, password, name, birthYear, phoneNo, gender);
    }

}