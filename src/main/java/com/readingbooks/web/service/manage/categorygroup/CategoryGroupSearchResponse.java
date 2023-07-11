package com.readingbooks.web.service.manage.categorygroup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryGroupSearchResponse {
    private Long id;
    private String name;
    private boolean isSearched;
}
