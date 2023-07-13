package com.readingbooks.web.controller.manage.book;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.book.BookManagementService;
import com.readingbooks.web.service.manage.book.BookRegisterRequest;
import com.readingbooks.web.service.manage.book.BookUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book")
public class BookManagementController {
    private final BookManagementService bookManagementService;

    @PostMapping
    public ResponseEntity<Object> register(BookRegisterRequest request,
                                               MultipartFile file){
        bookManagementService.register(request, file);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/image/{bookId}")
    public ResponseEntity<Object> update(MultipartFile file, @PathVariable Long bookId){
        bookManagementService.update(file, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/content/{bookId}")
    public ResponseEntity<Object> update(BookUpdateRequest request, @PathVariable Long bookId){
        bookManagementService.update(request, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookId){
        bookManagementService.delete(bookId);
        
        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
