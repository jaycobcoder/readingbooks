package com.readingbooks.web.controller.manage.category;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.category.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category-group")
    public ResponseEntity<Object> registerCategoryGroup(CategoryGroupRegisterRequest request){
        categoryService.registerCategoryGroup(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/category")
    public ResponseEntity<Object> registerCategory(CategoryRegisterRequest request){
        categoryService.registerCategory(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/category-group/{categoryGroupId}")
    public ResponseEntity<Object> updateCategoryGroup(@PathVariable Long categoryGroupId, CategoryGroupUpdateRequest request){
        categoryService.updateCategoryGroup(request, categoryGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/category/{categoryGroupId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long categoryId, CategoryUpdateRequest request){
        categoryService.updateCategory(request, categoryId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
