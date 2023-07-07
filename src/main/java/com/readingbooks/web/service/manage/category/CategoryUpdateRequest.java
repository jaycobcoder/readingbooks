package com.readingbooks.web.service.manage.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryUpdateRequest {
    private String name;
    private Long categoryGroupId;
}
