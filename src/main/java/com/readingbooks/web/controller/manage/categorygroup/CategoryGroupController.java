package com.readingbooks.web.controller.manage.categorygroup;

import com.readingbooks.web.controller.BaseResponse;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/category-group")
public class CategoryGroupController {

    private final CategoryGroupService categoryGroupService;

    @PostMapping
    public ResponseEntity<Object> registerCategoryGroup(CategoryGroupRegisterRequest request){
        categoryGroupService.register(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PatchMapping("/{categoryGroupId}")
    public ResponseEntity<Object> updateCategoryGroup(@PathVariable Long categoryGroupId, CategoryGroupUpdateRequest request){
        categoryGroupService.update(request, categoryGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{categoryGroupId}")
    public ResponseEntity<Object> deleteCategoryGroup(@PathVariable Long categoryGroupId){
        categoryGroupService.delete(categoryGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}