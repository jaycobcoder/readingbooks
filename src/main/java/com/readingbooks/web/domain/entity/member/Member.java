package com.readingbooks.web.domain.entity.member;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.domain.enums.MemberRole;
import jakarta.persistence.*;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;

    private String name;
    private String birthYear;
    private String phoneNo;
    private int point;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
