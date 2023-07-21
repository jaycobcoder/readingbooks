package com.readingbooks.web.service.book.dto;

import lombok.Getter;

@Getter
public class BookInformationResponse {
    private BookDto bookDto;
    private AuthorDto authorDto;

    public BookInformationResponse(BookDto bookDto, AuthorDto authorDto) {
        this.bookDto = bookDto;
        this.authorDto = authorDto;
    }

}
