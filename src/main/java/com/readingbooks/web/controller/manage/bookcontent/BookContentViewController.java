package com.readingbooks.web.controller.manage.bookcontent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookContentViewController {

    @GetMapping("/register/book-content")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBookContent");
        return "manage/bookcontent/bookcontent-register";
    }

    @GetMapping("/update/book-content")
    public String updateForm(Model model){
        model.addAttribute("selectFlag", "updateBookContent");
        return "manage/bookcontent/bookcontent-update";
    }

    @GetMapping("/delete/book-content")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteBookContent");
        return "manage/bookcontent/bookcontent-delete";
    }
}
