package com.readingbooks.web.controller.wishlist;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.order.OrderService;
import com.readingbooks.web.service.order.PayInformationResponse;
import com.readingbooks.web.service.wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final BookService bookService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Object> addBook(Long bookId, Principal principal){
        Member member = memberService.findMember(principal);
        Book book = bookService.findBook(bookId);
        wishlistService.addBook(member, book);

        BaseResponse response = new BaseResponse(
                HttpStatus.CREATED, "해당 도서가 위시리스트에 담겼습니다.", true
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteBook(@RequestParam List<Long> wishlistIdList, Principal principal){
        Long memberId = memberService.findMember(principal).getId();
        wishlistService.delete(wishlistIdList, memberId);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "도서를 위시리스트에서 제거했습니다.", true
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/pay-information")
    public ResponseEntity<Object> responsePayInformation(@RequestBody List<Long> bookIdList, Principal principal){
        Member member = memberService.findMember(principal);
        String email = member.getEmail();

        PayInformationResponse response = orderService.getPayInformation(bookIdList, email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
