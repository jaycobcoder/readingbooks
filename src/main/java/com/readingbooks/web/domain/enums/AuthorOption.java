package com.readingbooks.web.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorOption {
    AUTHOR("작가"), TRANSLATOR("번역가"), ILLUSTRATOR("삽화가");

    private final String korean;

}
