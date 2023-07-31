package com.readingbooks.web.repository.member;

import com.readingbooks.web.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    List<Member> findByNameAndPhoneNo(String name, String phoneNo);
}
