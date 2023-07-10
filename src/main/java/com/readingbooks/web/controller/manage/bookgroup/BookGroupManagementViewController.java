package com.readingbooks.web.controller.manage.bookgroup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookGroupManagementViewController {
    @GetMapping("/register/book-group")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBookGroup");
        return "manage/bookgroup/bookgroup-register";
    }

    @GetMapping("/update/book-group")
    public String updateBookGroup(Model model){
        model.addAttribute("selectFlag", "updateBookGroup");
        return "manage/bookgroup/bookgroup-update";
    }
}
