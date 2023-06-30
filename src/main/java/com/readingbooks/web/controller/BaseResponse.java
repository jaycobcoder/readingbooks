package com.readingbooks.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class BaseResponse {
    private final HttpStatus status;
    private final String message;
    private final boolean isSuccess;
}