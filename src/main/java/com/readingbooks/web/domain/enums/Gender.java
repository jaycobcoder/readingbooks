package com.readingbooks.web.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    MEN("남성"), WOMEN("여성"), SECRET("비공개");

    private final String korean;
}
