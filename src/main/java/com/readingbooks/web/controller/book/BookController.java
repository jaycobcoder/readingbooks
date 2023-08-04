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
        BookInformationResponse bookInformation = bookInformationService.findBookInformation(isbn);

        /* --- isbn에 해당하는 도서가 없다면 404 --- */
        if(bookInformation == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "error/404";
        }

        /* --- 시리즈 정보 --- */
        List<BookGroupInformationResponse> seriesInformation = bookInformationService.findSeriesInformation(isbn);

        /* --- 작가의 이름과 아이디 --- */
        List<AuthorNameAndIdResponse> authorNameAndIdList = bookInformationService.findAuthorNameAndIdList(isbn);

        /* --- 작가, 번역가, 삽화가의 국적, 소개와 같은 정보들 --- */
        AuthorInformationResponse authorInformation = null;
        Long authorId = bookInformation.getAuthorDto().getAuthorId();

        /* --- 만약 작가 아이디를 조작한 뒤 요청했다면, ControllerAdvice로 예외 처리를 하지 않기 위해 null로 반환 --- */
        try{
            authorInformation = bookInformationService.findAuthorInformation(isbn, authorId);
        } catch (AuthorNotfoundException e){
            authorInformation = null;
        }

        /* --- 내가 작성한 리뷰 --- */
        MyWroteReviewInBookResponse myReview = null;
        try{
            Member member = memberService.findMember(principal);
            Long memberId = member.getId();

            myReview = reviewService.findWroteReview(memberId, isbn);
            model.addAttribute("isLogin", true);
        } catch (NotLoginException e){
            model.addAttribute("isLogin", false);
        }

        /* --- 도서에 작성된 리뷰 --- */
        List<ReviewResponse> reviews = reviewService.findReviews(isbn);
        if(reviews.size() != 0){
            /* --- 몇 명이 평가했고, 평점의 평균 구하기 --- */
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

        /* --- 본인 확인용 이메일 --- */
        String email = "";
        if(principal != null){
            email = principal.getName();
        }else{
            email = "";
        }
        model.addAttribute("email", email);

        model.addAttribute("myReview", myReview);
        model.addAttribute("reviews", reviews);
        model.addAttribute("information", bookInformation);
        model.addAttribute("booksInGroup", seriesInformation);
        model.addAttribute("authorNameAndIdList", authorNameAndIdList);
        model.addAttribute("authorInformation", authorInformation);
        return "book/book";
    }

    @GetMapping("/book/{isbn}/{authorId}")
    @ResponseBody
    public ResponseEntity<Object> getAuthorInformation(@PathVariable String isbn, @PathVariable Long authorId){
        AuthorInformationResponse authorInformation = bookInformationService.findAuthorInformation(isbn, authorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(authorInformation);
    }
}
