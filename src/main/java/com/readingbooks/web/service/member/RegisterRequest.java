package com.readingbooks.web.service.member;

import com.readingbooks.web.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthYear;
    private Gender gender;
    private String phoneNo;
}
