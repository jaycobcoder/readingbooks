package com.readingbooks.web.service.book.dto;

import lombok.Getter;

@Getter
public class AuthorDto {
    private Long authorId;
    private String author;
    private Long translatorId;
    private String translator;
    int authorCountExceptMainAuthor;

    public AuthorDto(Long authorId, String author, Long translatorId, int authorCountExceptMainAuthor, String translator) {
        this.authorId = authorId;
        this.author = author;
        this.translatorId = translatorId;
        this.translator = translator;
        this.authorCountExceptMainAuthor = authorCountExceptMainAuthor;
    }
}