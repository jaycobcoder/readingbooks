package com.readingbooks.web.controller.order;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.order.OrderRequest;
import com.readingbooks.web.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;

    @PostMapping("/order")
    public ResponseEntity<Object> order(@ModelAttribute OrderRequest request, Principal principal) {
        Member member = memberService.findMember(principal);
        List<Book> books = bookService.findAllById(request.getBookIdList());

        orderService.order(member, books, request);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK, "주문이 완료되었습니다", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(baseResponse);
    }
}
