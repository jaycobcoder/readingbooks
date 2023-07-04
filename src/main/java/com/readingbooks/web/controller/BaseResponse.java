package com.readingbooks.web.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class BaseResponse {
    private final HttpStatus status;
    private final String message;
    private final boolean isSuccess;
}