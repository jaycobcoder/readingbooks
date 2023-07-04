package com.readingbooks.web.controller;

import com.readingbooks.web.exception.base.NotFoundException;
import com.readingbooks.web.exception.base.PresentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(PresentException.class)
    public ResponseEntity<Object> handlePresentException(Exception e){
        log.info("예외 발생 - PresentException : ", e.getMessage());

        BaseResponse response = new BaseResponse(HttpStatus.CONFLICT, e.getMessage(), false);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFoundException(Exception e){
        log.info("예외 발생 - NotFoundException : ", e.getMessage());

        BaseResponse response = new BaseResponse(HttpStatus.NOT_FOUND, e.getMessage(), false);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handlerIllegalArgumentException(Exception e){
        log.info("예외 발생 - IllegalArgumentException : ", e.getMessage());

        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST, e.getMessage(), false);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
