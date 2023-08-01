package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.orders.Orders;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.member.MemberPresentException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.like.ReviewLikeLogRepository;
import com.readingbooks.web.repository.member.MemberRepository;
import com.readingbooks.web.repository.orders.OrdersRepository;
import com.readingbooks.web.repository.review.ReviewRepository;
import com.readingbooks.web.repository.reviewcomment.ReviewCommentRepository;
import com.readingbooks.web.service.mail.MailService;
import com.readingbooks.web.service.order.OrderRequest;
import com.readingbooks.web.service.order.OrderService;
import com.readingbooks.web.service.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class MemberServiceTest {
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewCommentRepository reviewCommentRepository;
    @Autowired
    private ReviewLikeLogRepository reviewLikeLogRepository;
    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void beforeEach(){
        MailService mailService = mock(MailService.class);
        memberService = new MemberService(memberRepository, passwordEncoder, mailService, ordersRepository, reviewRepository, reviewCommentRepository, reviewLikeLogRepository, reviewService, bookRepository);
    }

    @Test
    void whenRegisteringFormNull_thenThrowException(){
        RegisterRequest request = createRequest(null, null, null, null, null, null, null);
        assertThatThrownBy(
                ()-> memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenRegisteringInvalidEmail_thenThrowException(){
        RegisterRequest request = createRequest("tes@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일을 올바르게 입력해주세요.");

    }

    @Test
    void whenRegisteringInvalidPasswordLength_thenThrowException(){
        RegisterRequest request = createRequest("test@example.com", "test","test", "test", "1999", Gender.SECRET, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void whenRegisteringNotMatchedPasswords_thenThrowException(){
        RegisterRequest request = createRequest("test@example.com", "test1234", "helloworld1234", "test1234", "1999", Gender.SECRET, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void whenRegisteringInvalidNameLength_thenThrowException(){
        RegisterRequest request = createRequest("test@example.com", "test1234", "test1234","t", "1999",  Gender.SECRET, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringInvalidBirthyearLength_thenThrowException(){
        RegisterRequest request = createRequest("test@example.com", "test1234!", "test1234!", "test", "19990115",  Gender.SECRET, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringGenderNull_thenThrowException(){
        RegisterRequest request = createRequest("test@example.com", "test1234!", "test1234!","test", "1999",  null, "01012341234");
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("성별을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringExistedEmail_thenThrowException(){
        //given
        RegisterRequest registerMember = createRequest("test@example.com", "test1234!","test1234!", "test", "1999",  Gender.SECRET, "01012341234");
        memberService.register(registerMember);

        RegisterRequest request = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.MEN, "01012341234");

        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(MemberPresentException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
    void whenRegisteringInValidPhoneNo_thenThrowException(){
        RegisterRequest blankPhoneNo = createRequest("test@example.com", "test1234!", "test1234!", "test", "1999",  Gender.SECRET, "");
        RegisterRequest nullPhoneNo = createRequest("test@example.com", "test1234!", "test1234!", "test", "1999",  Gender.SECRET, null);
        RegisterRequest lengthPhoneNo = createRequest("test@example.com", "test1234!", "test1234!", "test", "1999",  Gender.SECRET, "010");
        assertThatThrownBy(()->memberService.register(blankPhoneNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸드폰 번호를 올바르게 입력해주세요.");
        assertThatThrownBy(()->memberService.register(nullPhoneNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸드폰 번호를 올바르게 입력해주세요.");
        assertThatThrownBy(()->memberService.register(lengthPhoneNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸드폰 번호를 올바르게 입력해주세요.");
    }

    @Test
    void maskingTest(){
        String email = "test1234@gmail.com";
        String maskedEmail = memberService.maskEmail(email);

        assertThat(maskedEmail).isEqualTo("tes*****@gmail.com");
    }

    @Test
    void whenMemberRegistered_thenVerifyFields(){
        //given
        RegisterRequest request = createRequest("success@example.com", "test1234!", "test1234!","test", "1999",  Gender.SECRET, "01012341234");

        //when
        Long memberId = memberService.register(request);
        Member findMember = memberService.findMember(memberId);

        //then
        assertThat(findMember.getEmail()).isEqualTo("success@example.com");
        assertThat(passwordEncoder.matches(request.getPassword(), findMember.getPassword())).isTrue();
        assertThat(findMember.getName()).isEqualTo("test");
        assertThat(findMember.getBirthYear()).isEqualTo("1999");
        assertThat(findMember.getGender()).isEqualTo(Gender.SECRET);
    }

    @Test
    void whenChangedTempPassword_thenVerifyField(){
        //given
        RegisterRequest request = new RegisterRequest("test@gmail.com", "testtest1234", "testtest1234", "홍길동", "1999", Gender.SECRET, "01055555555");
        Long memberId = memberService.register(request);

        //when
        String email = "test@gmail.com";
        String tempPassword = memberService.changePasswordAndSendEmail(email, "01055555555");

        Member member = memberService.findMember(memberId);


        //then
        assertThat(passwordEncoder.matches(tempPassword, member.getPassword())).isTrue();
    }

    @Test
    void whenLeavedMember_thenVerifyReview(){
        //given
        /* --- 회원가입 --- */
        Member member = getMember();

        /* --- 도서 등록 --- */
        Book book = bookRepository.save(new Book());

        /* --- 리뷰 작성 --- */
        Review review = reviewRepository.save(Review.createReview(member, book, "test", 5, true));

        //when
        boolean isDeleted = memberService.leave(member, "testtest1234");
        Optional<Review> findReview = reviewRepository.findById(review.getId());

        assertThat(isDeleted).isTrue();
        assertThat(findReview.isEmpty()).isTrue();
    }

    @Test
    void whenLeavedMember_thenVerifyOrders(){
        //given
        /* --- 회원가입 --- */
        Member member = getMember();

        /* --- 도서 등록 --- */
        bookRepository.save(new Book());

        /* --- 주문 상황 --- */
        List bookIdList = new ArrayList<>();
        bookIdList.add(1L);
        Orders orders = Orders.createOrders(member, new OrderRequest("test", "test", "test", "test", "test", 1000, 100, 900, bookIdList));
        ordersRepository.save(orders);

        //when
        memberService.leave(member, "testtest1234");
        Orders findOrder = ordersRepository.findById(orders.getId()).get();

        //then
        assertThat(findOrder.getMember()).isNull();
    }

    private Member getMember() {
        RegisterRequest request = new RegisterRequest("test@gmail.com", "testtest1234", "testtest1234", "홍길동", "1999", Gender.SECRET, "01055555555");
        Long memberId = memberService.register(request);
        return memberService.findMember(memberId);
    }

    private RegisterRequest createRequest(String email, String password, String passwordConfirm, String name, String birthYear, Gender gender, String phoneNo){
        return new RegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }
}