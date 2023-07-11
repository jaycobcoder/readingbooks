package com.readingbooks.web.service.manage.bookcontent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookContentRegisterRequest {
    private Long bookId;
    private String content;
}
