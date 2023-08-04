package com.readingbooks.web.security.authenticationprovider;

import com.readingbooks.web.exception.login.LoginCheckFailException;
import com.readingbooks.web.security.userdetailsservice.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadingBooksAuthenticationProvider implements AuthenticationProvider {
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /* --- 아이디 관련 검증(UserDetailsService) --- */
        String email = authentication.getName();
        UserDetails memberDetails = memberDetailsService.loadUserByUsername(email);

        /* --- 비밀번호 관련 검증(PasswordEncoder) --- */
        String credential = matchPassword(authentication, memberDetails);
        return new UsernamePasswordAuthenticationToken(email, credential, memberDetails.getAuthorities());
    }

    private String matchPassword(Authentication authentication, UserDetails memberDetails) {
        String rawPassword = authentication.getCredentials().toString();
        String hashPassword = memberDetails.getPassword();
        checkPassword(rawPassword, hashPassword);
        return rawPassword;
    }

    private void checkPassword(String rawPassword, String hashPassword) {
        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, hashPassword);
        if(isPasswordMatch == false){
            throw new LoginCheckFailException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
