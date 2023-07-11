package com.readingbooks.web.controller.manage.categorygroup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryGroupViewController {

    @GetMapping("/register/category-group")
    public String registerCategoryGroup(Model model){
        model.addAttribute("selectFlag", "registerCategoryGroup");
        return "manage/category/categorygroup-register";
    }

    @GetMapping("/update/category-group")
    public String updateCategoryGroup(Model model){
        model.addAttribute("selectFlag", "updateCategoryGroup");
        return "manage/category/categorygroup-update";
    }
}