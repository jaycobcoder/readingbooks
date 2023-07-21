package com.readingbooks.web.service.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorNameAndIdResponse {
    private String name;
    private Long id;
    private String option;
}
