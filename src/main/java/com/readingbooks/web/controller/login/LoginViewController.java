package com.readingbooks.web.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/account")
public class LoginViewController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) boolean hasMessage,
                        @RequestParam(required = false) String message,
                        @RequestParam(required = false) String returnUrl,
                        HttpServletRequest request,
                        Model model){
        HttpSession session = request.getSession();
        session.setAttribute("returnUrl", returnUrl);

        model.addAttribute("hasMessage", hasMessage);
        model.addAttribute("message", message);
        return "login/login";
    }

    @GetMapping("/register")
    public String register(){
        return "/login/register";
    }

    @GetMapping("/register/email")
    public String registerForm(Model model, HttpServletRequest request){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        model.addAttribute("csrfToken", token);
        return "/login/email-register";
    }
}
