package com.readingbooks.web.controller.manage.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryViewController {

    @GetMapping("/register/category")
    public String registerCategory(Model model){
        model.addAttribute("selectFlag", "registerCategory");
        return "manage/category/category-register";
    }

    @GetMapping("/update/category")
    public String updateCategory(Model model){
        model.addAttribute("selectFlag", "updateCategory");
        return "manage/category/category-update";
    }
}
