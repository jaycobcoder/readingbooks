package com.readingbooks.web.controller.manage.bookcontent;

import com.readingbooks.web.service.manage.book.BookUpdateResponse;
import com.readingbooks.web.service.manage.bookcontent.BookContentService;
import com.readingbooks.web.service.manage.bookcontent.BookContentUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookContentViewController {

    private final BookContentService bookContentService;

    @GetMapping("/register/book-content")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBookContent");
        return "manage/bookcontent/bookcontent-register";
    }

    @GetMapping("/update/search/book-content")
    public String updateSearchForm(Model model){
        model.addAttribute("selectFlag", "updateBookContent");
        return "manage/bookcontent/bookcontent-update-search";
    }

    @GetMapping("/update/book-content")
    public String updateForm(Model model, Long bookId){
        BookContentUpdateResponse response = bookContentService.searchBookContent(bookId);

        if(response == null){
            model.addAttribute("isSearched", false);
        } else{
            model.addAttribute("isSearched", true);
        }

        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "updateBookContent");
        return "manage/bookcontent/bookcontent-update";
    }

    @GetMapping("/delete/book-content")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteBookContent");
        return "manage/bookcontent/bookcontent-delete";
    }
}
