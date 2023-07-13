package com.readingbooks.web.controller.manage.author;

import com.readingbooks.web.service.manage.author.AuthorManagementService;
import com.readingbooks.web.service.manage.author.AuthorSearchResponse;
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
public class AuthorManagementViewController {

    private final AuthorManagementService authorManagementService;

    @GetMapping("/register/author")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerAuthor");
        return "manage/author/author-register";
    }

    @GetMapping("/update/author")
    public String updateForm(Model model){
        model.addAttribute("selectFlag", "updateAuthor");
        return "manage/author/author-update";
    }

    @GetMapping("/search/author")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchAuthor");
        return "manage/author/author-search";
    }

    @GetMapping("/result/author")
    public String returnSearchResult(@RequestParam String name, Model model){
        List<AuthorSearchResponse> response = authorManagementService.searchAuthor(name);

        model.addAttribute("responses", response);
        model.addAttribute("search", name);
        model.addAttribute("selectFlag", "searchAuthor");
        return "manage/author/author-result";
    }

    @GetMapping("/delete/author")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteAuthor");
        return "manage/author/author-delete";
    }
}
