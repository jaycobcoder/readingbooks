package com.readingbooks.web.controller.manage.categorygroup;

import com.readingbooks.web.service.manage.categorygroup.CategoryGroupSearchResponse;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryGroupViewController {

    private final CategoryGroupService categoryGroupService;

    @GetMapping("/register/category-group")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerCategoryGroup");
        return "manage/categorygroup/categorygroup-register";
    }

    @GetMapping("/update/category-group")
    public String updateForm(Model model){
        model.addAttribute("selectFlag", "updateCategoryGroup");
        return "manage/categorygroup/categorygroup-update";
    }

    @GetMapping("/search/category-group")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchCategoryGroup");
        return "manage/categorygroup/categorygroup-search";
    }

    @GetMapping("/result/category-group")
    public String returnSearchResult(@RequestParam String name, Model model){
        CategoryGroupSearchResponse response = categoryGroupService.searchCategoryName(name);

        if(response.isSearched() == false){
            model.addAttribute("isSearched", false);
        }

        model.addAttribute("isSearched", true);
        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "searchCategoryGroup");
        return "manage/categorygroup/categorygroup-result";
    }

    @GetMapping("/delete/category-group")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteCategoryGroup");
        return "manage/categorygroup/categorygroup-delete";
    }
}