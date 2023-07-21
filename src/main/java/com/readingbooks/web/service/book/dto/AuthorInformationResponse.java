package com.readingbooks.web.service.book.dto;

import com.readingbooks.web.domain.entity.author.Author;
import lombok.Getter;

@Getter
public class AuthorInformationResponse {
    private Long id;
    private String name;
    private String option;
    private String nationality;
    private String description;
    private String birthYear;
    private String gender;

    public AuthorInformationResponse(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.option = author.getAuthorOption().toString();
        this.nationality = author.getNationality();
        this.description = author.getDescription();
        this.birthYear = author.getBirthYear();
        this.gender = "남성";

        switch (author.getAuthorOption().toString()){
            case "WOMEN" :
                gender = "여성";
                break;
            case "SECRET" :
                gender = "미상";
                break;
        }
    }
}
