package com.readingbooks.web.controller.manage.author;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.author.AuthorManagementService;
import com.readingbooks.web.service.manage.author.AuthorRegisterRequest;
import com.readingbooks.web.service.manage.author.AuthorUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/author")
public class AuthorManagementController {
    private final AuthorManagementService authorManagementService;

    @PostMapping
    public ResponseEntity<Object> registerAuthor(AuthorRegisterRequest request){
        authorManagementService.registerAuthor(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{authorId}")
    public ResponseEntity<Object> updateAuthor(AuthorUpdateRequest request,
                                               @PathVariable Long authorId){
        authorManagementService.updateAuthor(request, authorId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
