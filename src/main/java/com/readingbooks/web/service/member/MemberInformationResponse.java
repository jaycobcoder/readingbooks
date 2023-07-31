package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.member.Member;
import lombok.Getter;

@Getter
public class MemberInformationResponse {
    private String name;
    private String email;

    public MemberInformationResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
