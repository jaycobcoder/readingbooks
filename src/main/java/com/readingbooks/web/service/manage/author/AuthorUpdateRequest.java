package com.readingbooks.web.service.manage.author;

import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorUpdateRequest {
    private String name;
    private AuthorOption authorOption;
    private String nationality;
    private String description;
    private String birthYear;
    private Gender gender;
}
