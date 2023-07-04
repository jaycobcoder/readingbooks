package com.readingbooks.web.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginViewController {
    @GetMapping("/account/login")
    public String login(@RequestParam(required = false) boolean hasMessage,
                        @RequestParam(required = false) String message,
                        Model model){

        model.addAttribute("hasMessage", hasMessage);
        model.addAttribute("message", message);
        return "login/login";
    }

    @GetMapping("/account/register")
    public String register(){
        return "/login/register";
    }

    @GetMapping("/account/register/email")
    public String registerForm(Model model, HttpServletRequest request){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        model.addAttribute("csrfToken", token);
        return "/login/email-register";
    }
}
