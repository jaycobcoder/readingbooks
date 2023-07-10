package com.readingbooks.web.controller.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManagementHomeController {
    @GetMapping("/home")
    public String home(){
        return "manage/home";
    }
}
