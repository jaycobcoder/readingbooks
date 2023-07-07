package com.readingbooks.web.controller.manage.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage")
public class CategoryViewController {

    @GetMapping("/category-group")
    public String registerCategoryGroup(){
        return "";
    }
}
