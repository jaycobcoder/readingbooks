package com.readingbooks.web.security.userdetailsservice;

import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.exception.login.LoginCheckFailException;
import com.readingbooks.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new LoginCheckFailException());

        return new MemberDetails(member);
    }
}
