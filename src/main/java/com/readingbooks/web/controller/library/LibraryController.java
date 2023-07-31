package com.readingbooks.web.controller.library;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.exception.library.NotBoughtBookException;
import com.readingbooks.web.service.library.BookContentResponse;
import com.readingbooks.web.service.library.LibraryResponse;
import com.readingbooks.web.service.library.LibraryService;
import com.readingbooks.web.service.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;
    private final MemberService memberService;

    @GetMapping
    public String library(Principal principal, Model model){
        Member member = memberService.findMember(principal);
        List<LibraryResponse> responses = libraryService.findBooksInLibrary(member);

        model.addAttribute("responses", responses);
        return "user/library/library";
    }

    @GetMapping("/{bookId}")
    public String bookContent(Principal principal, @PathVariable Long bookId, Model model, HttpServletResponse response) throws IOException {
        Member member = memberService.findMember(principal);


        BookContentResponse bookContentResponse = null;

        try{
            bookContentResponse = libraryService.findBookContent(member, bookId);
        } catch (NotBoughtBookException e) {
            /* --- 본인이 구매하지 않은 도서를 조회할 시 --- */
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "/error/403";
        }

        model.addAttribute("response", bookContentResponse);
        return "user/library/content";
    }
}
