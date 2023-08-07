package com.readingbooks.web.controller.book;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.exception.login.NotLoginException;
import com.readingbooks.web.service.book.*;
import com.readingbooks.web.service.book.dto.AuthorInformationResponse;
import com.readingbooks.web.service.book.dto.AuthorNameAndIdResponse;
import com.readingbooks.web.service.book.dto.BookGroupInformationResponse;
import com.readingbooks.web.service.book.dto.BookInformationResponse;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.review.MyWroteReviewInBookResponse;
import com.readingbooks.web.service.review.ReviewResponse;
import com.readingbooks.web.service.review.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookInformationService bookInformationService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @GetMapping("/book/{isbn}")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String book(@PathVariable String isbn, HttpServletResponse response, Principal principal, Model model) throws IOException {
        /* --- 도서 정보 --- */
        BookInformationResponse bookInformation = findBookInformation(isbn, response);
        if (bookInformation == null) {
            return "error/404";
        }
        model.addAttribute("information", bookInformation);

        /* --- 도서 그룹에 포함되어있는 도서들 --- */
        List<BookGroupInformationResponse> seriesInformation = bookInformationService.findSeriesInformation(isbn);
        model.addAttribute("booksInGroup", seriesInformation);

        /* --- 작가의 이름과 아이디 --- */
        List<AuthorNameAndIdResponse> authorNameAndIdList = bookInformationService.findAuthorNameAndIdList(isbn);
        model.addAttribute("authorNameAndIdList", authorNameAndIdList);

        /* --- 작가, 번역가, 삽화가의 국적, 소개와 같은 정보들 --- */
        AuthorInformationResponse authorInformation = findAuthorInformation(isbn, bookInformation);
        model.addAttribute("authorInformation", authorInformation);

        /* --- 내가 작성한 리뷰 --- */
        MyWroteReviewInBookResponse myReview = findMyWroteReviewWhenMemberLoggedIn(isbn, principal, model);
        model.addAttribute("myReview", myReview);

        /* --- 도서에 작성된 리뷰, 리뷰의 평점과 몇 명 평가했는지 정보 --- */
        List<ReviewResponse> reviews = reviewService.findReviews(isbn);
        model.addAttribute("reviews", reviews);
        addAttributeTotalReviewRatingAndReviewCount(model, reviews);

        /* --- 본인 확인용 이메일 --- */
        String email = findEmailForCheckingIdentification(principal);
        model.addAttribute("email", email);

        return "book/book";
    }

    @GetMapping("/book/{isbn}/{authorId}")
    @ResponseBody
    public ResponseEntity<Object> getAuthorInformation(@PathVariable String isbn, @PathVariable Long authorId){
        AuthorInformationResponse authorInformation = bookInformationService.findAuthorInformation(isbn, authorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(authorInformation);
    }

    /**
     * 리뷰를 몇 명이 평가했고, 평점의 평균 구하는 메소드
     * @param model
     * @param reviews
     */
    private void addAttributeTotalReviewRatingAndReviewCount(Model model, List<ReviewResponse> reviews) {
        if(reviews.size() != 0){
            int totalReviewRating = 0;
            int reviewCount = 0;
            for (ReviewResponse review : reviews) {
                reviewCount++;
                totalReviewRating += review.getStarRating();
            }
            double starRatingAvg = (totalReviewRating / (reviewCount * 1.0));
            model.addAttribute("starRatingAvg", starRatingAvg);
            model.addAttribute("reviewCount", reviewCount);
        }else{
            model.addAttribute("starRatingAvg", 0.0);
            model.addAttribute("reviewCount", 0);
        }
    }

    /**
     * 댓글 삭제 버튼 등은 로그인한 정보 Principal에서 email과 댓글을 작성한 email이 일치할 때 버튼을 렌더링 하기에 email 정보를 응답하는 메소드
     * @param principal
     * @return email
     */
    private String findEmailForCheckingIdentification(Principal principal) {
        String email = "";
        if(principal != null){
            email = principal.getName();
        }else{
            email = "";
        }
        return email;
    }

    /**
     * 만약 로그인했다면 내가 작성한 리뷰를 응답
     * @param isbn
     * @param principal
     * @param model
     * @return
     */
    private MyWroteReviewInBookResponse findMyWroteReviewWhenMemberLoggedIn(String isbn, Principal principal, Model model) {
        MyWroteReviewInBookResponse myReview = null;
        try{
            Member member = memberService.findMember(principal);

            Long memberId = member.getId();

            myReview = reviewService.findWroteReview(memberId, isbn);
            model.addAttribute("isLogin", true);
        } catch (NotLoginException e){
            model.addAttribute("isLogin", false);
        }
        return myReview;
    }

    /**
     * 작가의 정보를 응답하는 메소드
     * @param isbn
     * @param bookInformation
     * @return AuthorInformation Dto
     */
    private AuthorInformationResponse findAuthorInformation(String isbn, BookInformationResponse bookInformation) {
        AuthorInformationResponse authorInformation = null;
        Long authorId = bookInformation.getAuthorDto().getAuthorId();

        /* --- 만약 작가 아이디를 조작한 뒤 요청했다면, ControllerAdvice로 예외 처리를 하지 않기 위해 null로 반환 --- */
        try{
            authorInformation = bookInformationService.findAuthorInformation(isbn, authorId);
        } catch (AuthorNotfoundException e){
            authorInformation = null;
        }
        return authorInformation;
    }

    /**
     * 도서의 정보를 응답하는 메소드
     * @param isbn
     * @param response
     * @return BookInformation Dto
     * @throws IOException
     */
    private BookInformationResponse findBookInformation(String isbn, HttpServletResponse response) throws IOException {
        BookInformationResponse bookInformation = bookInformationService.findBookInformation(isbn);

        /* --- isbn에 해당하는 도서가 없다면 404 --- */
        if(bookInformation == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return bookInformation;
    }
}
