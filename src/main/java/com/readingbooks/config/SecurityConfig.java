package com.readingbooks.config;

import com.readingbooks.web.service.rememberme.RememberMeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RememberMeUserDetailsService rememberMeService;
    private static final int ONE_MONTH = 2678400;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        return http
                .csrf(csrf -> csrf
                    .csrfTokenRequestHandler(requestHandler)
                    .ignoringRequestMatchers("/register/**", "/account/login/**", "/logout/**")
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRepository(csrfTokenRepository())
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/cart").hasRole("MEMBER")
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(customizer -> customizer
                        .loginPage("/account/login")
                        .loginProcessingUrl("/account/login")
                        .usernameParameter("email")
                        .successHandler(new LoginSuccessHandler("/"))
                        .failureHandler(new LoginFailHandler())
                        .permitAll()
                )
                .rememberMe(customizer -> customizer
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(ONE_MONTH)
                        .userDetailsService(rememberMeService)
                        .authenticationSuccessHandler(new LoginSuccessHandler("/"))
                )
                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("remember-me")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder (){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public HttpSessionCsrfTokenRepository csrfTokenRepository(){
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setHeaderName("X-CSRF-TOKEN");
        csrfTokenRepository.setParameterName("_csrf");
        csrfTokenRepository.setSessionAttributeName("XSRF-TOKEN");
        return csrfTokenRepository;
    }
}
