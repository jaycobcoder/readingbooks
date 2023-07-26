package com.readingbooks.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        super.clearAuthenticationAttributes(request);

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        HttpSession session = request.getSession();
        String returnUrl = session.getAttribute("returnUrl") == null ? "" : session.getAttribute("returnUrl").toString();
        session.removeAttribute("returnUrl");

        if(savedRequest != null){
            String url = savedRequest.getRedirectUrl();
            if(url == null || url.equals("")){
                url = "/";
            }
            if(url.contains("/register")){
                url = "/";
            }
            if(url.contains("/account/login")){
                url = "/";
            }
            requestCache.removeRequest(request, response);
            getRedirectStrategy().sendRedirect(request, response, url);
        }

        if(!returnUrl.equals("")){
            String url = returnUrl;
            getRedirectStrategy().sendRedirect(request, response, url);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
