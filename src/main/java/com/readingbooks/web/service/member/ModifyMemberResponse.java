package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.entity.member.Member;
import lombok.Getter;

@Getter
public class ModifyMemberResponse {
    private Long memberId;
    private String name;
    private String email;

    public ModifyMemberResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
