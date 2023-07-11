package com.readingbooks.web.controller.manage.bookcontent;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.bookcontent.BookContentRegisterRequest;
import com.readingbooks.web.service.manage.bookcontent.BookContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book-content")
public class BookContentController {
    private final BookContentService bookContentService;

    @PostMapping
    public ResponseEntity<Object> register(BookContentRegisterRequest request){
        bookContentService.register(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    @PatchMapping("/{bookId}")
    public ResponseEntity<Object> update(@PathVariable Long bookId, String content){
        bookContentService.update(bookId, content);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookId, String content){
        bookContentService.delete(bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}