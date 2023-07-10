package com.readingbooks.web.controller.manage.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookManagementViewController {

    @GetMapping("/register/book")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBook");
        return "manage/book/book-register";
    }


    @GetMapping("/update/book")
    public String updateBook(Model model){
        model.addAttribute("selectFlag", "updateBook");
        return "manage/book/book-update";
    }
}
