package com.readingbooks.web.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class MemberViewController {

    @GetMapping("/home")
    public String home(){
        return "user/home";
    }

    @GetMapping("/modify")
    public String modifyForm(Model model){
        model.addAttribute("active", "modifyAccount");
        return "/user/modify/modify";
    }
}