package com.readingbooks.web.service.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookGroupInformationResponse {
    private String isbn;
    private String title;
    private String savedImageName;
}
