package com.readingbooks.web.controller.manage.book;

import com.readingbooks.web.service.manage.book.BookManagementService;
import com.readingbooks.web.service.manage.book.BookManageSearchResponse;
import com.readingbooks.web.service.manage.book.BookUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookManagementViewController {

    private final BookManagementService bookManagementService;

    @GetMapping("/register/book")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBook");
        return "manage/book/book-register";
    }

    @GetMapping("/update/search/book")
    public String updateSearchForm(Model model){
        model.addAttribute("selectFlag", "updateBook");
        return "manage/book/book-update-search";
    }

    @GetMapping("/update/book")
    public String updateForm(Model model, Long bookId){
        BookUpdateResponse response = bookManagementService.searchBook(bookId);

        if(response == null){
            model.addAttribute("isSearched", false);
        } else{
            model.addAttribute("isSearched", true);
        }

        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "updateBook");
        return "manage/book/book-update";
    }

    @GetMapping("/search/book")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchBook");
        return "manage/book/book-search";
    }

    @GetMapping("/result/book")
    public String returnSearchResult(@RequestParam String title, Model model){
        List<BookManageSearchResponse> responses = bookManagementService.searchBook(title);

        model.addAttribute("responses", responses);
        model.addAttribute("search", title);
        model.addAttribute("selectFlag", "searchBook");
        return "manage/book/book-result";
    }

    @GetMapping("/delete/book")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteBook");
        return "manage/bookgroup/book-delete";
    }
}