package com.readingbooks.web.controller.manage.bookauthorlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookAuthorListViewController {

    @GetMapping("/register/book-author-list")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBookAuthorList");
        return "manage/bookauthorlist/bookauthorlist-register";
    }

    @GetMapping("/delete/book-author-list")
    public String updateBook(Model model){
        model.addAttribute("selectFlag", "deleteBookAuthorList");
        return "manage/bookauthorlist/bookauthorlist-delete";
    }
}
