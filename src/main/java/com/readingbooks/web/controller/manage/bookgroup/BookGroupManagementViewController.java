package com.readingbooks.web.controller.manage.bookgroup;

import com.readingbooks.web.service.manage.bookgroup.BookGroupManagementService;
import com.readingbooks.web.service.manage.bookgroup.BookGroupSearchResponse;
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
public class BookGroupManagementViewController {

    private final BookGroupManagementService bookGroupManagementService;

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

    @GetMapping("/search/book-group")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchBookGroup");
        return "manage/bookgroup/bookgroup-search";
    }

    @GetMapping("/result/book-group")
    public String returnSearchResult(@RequestParam String title, Model model){
        List<BookGroupSearchResponse> responses = bookGroupManagementService.searchBookGroup(title);

        model.addAttribute("responses", responses);
        model.addAttribute("search", title);
        model.addAttribute("selectFlag", "searchBookGroup");
        return "manage/bookgroup/bookgroup-result";
    }

    @GetMapping("/delete/book-group")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteBookGroup");
        return "manage/bookgroup/bookgroup-delete";
    }
}
