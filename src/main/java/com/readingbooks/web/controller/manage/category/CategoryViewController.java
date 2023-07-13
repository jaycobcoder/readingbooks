package com.readingbooks.web.controller.manage.category;

import com.readingbooks.web.service.manage.category.CategorySearchResponse;
import com.readingbooks.web.service.manage.category.CategoryService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
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
public class CategoryViewController {

    private final CategoryService categoryService;
    private final CategoryGroupService categoryGroupService;

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

    @GetMapping("/search/category")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchCategory");
        return "manage/category/category-search";
    }

    @GetMapping("/result/category")
    public String resultForm(@RequestParam String name, Model model){
        CategorySearchResponse response = categoryService.searchByCategoryName(name);

        if(response.isSearched() == false){
            model.addAttribute("isSearched", false);
        }

        model.addAttribute("isSearched", true);
        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "searchCategory");
        return "manage/category/category-result";
    }

    @GetMapping("/delete/category")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteCategory");
        return "manage/category/category-delete";
    }

    @GetMapping("/search/categories")
    public String resultForm(Model model){
        List<CategorySearchResponse> responses = categoryService.searchCategories();

        model.addAttribute("responses", responses);
        model.addAttribute("selectFlag", "searchCategories");
        return "manage/category/categories";
    }
}
