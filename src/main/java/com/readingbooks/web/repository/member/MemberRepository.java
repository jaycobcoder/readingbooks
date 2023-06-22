package com.readingbooks.web.repository.member;

import com.readingbooks.web.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
