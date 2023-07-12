package com.readingbooks.web.controller.manage.bookauthorlist;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListRegisterRequest;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book-author-list")
public class BookAuthorListController {
    private final BookAuthorListService bookAuthorListService;

    @PostMapping
    public ResponseEntity<Object> register(BookAuthorListRegisterRequest request){
        bookAuthorListService.register(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestParam Long bookId,
                                         @RequestParam Long authorId){
        bookAuthorListService.delete(bookId, authorId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "제거가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
