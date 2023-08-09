package com.readingbooks.web.domain.entity.member;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.library.Library;
import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.domain.enums.MemberRole;
import com.readingbooks.web.service.member.RegisterRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Wishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Library> libraries = new ArrayList<>();

    public static Member createMember(RegisterRequest request){
        Member member = new Member();
        member.email = request.getEmail();
        member.password = request.getPassword();
        member.name = request.getName();
        member.birthYear = request.getBirthYear();
        member.gender = request.getGender();
        member.role = MemberRole.ROLE_MEMBER;
        member.phoneNo = request.getPhoneNo();
        return member;
    }

    public void encodePassword(String encodedPassword){
        password = encodedPassword;
    }

    public void updatePassword(String changingPassword) {
        password = changingPassword;
    }
}
