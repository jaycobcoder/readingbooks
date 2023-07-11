package com.readingbooks.web.service.manage.category;

import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.category.CategoryGroupRepository;
import com.readingbooks.web.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;

    public Long registerCategoryGroup(CategoryGroupRegisterRequest request) {
        validateCategoryGroupRegisterForm(request);
        CategoryGroup categoryGroup = CategoryGroup.createCategoryGroup(request);
        return categoryGroupRepository.save(categoryGroup).getId();
    }

    public Long registerCategory(CategoryRegisterRequest request){
        Long categoryGroupId = request.getCategoryGroupId();
        String name = request.getName();

        validateCategoryRegisterForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);

        Category category = Category.createCategory(name, categoryGroup);
        return categoryRepository.save(category).getId();
    }

    public CategoryGroup findCategoryGroupById(Long categoryGroupId) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId)
                .orElseThrow(() -> new CategoryNotFoundException("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요."));
        return categoryGroup;


    }
    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요."));
        return category;
    }

    private void validateCategoryRegisterForm(CategoryRegisterRequest request) {
        String name = request.getName();
        validateName(name, "카테고리를 입력하세요.");

        Long categoryGroupId = request.getCategoryGroupId();
        if(categoryGroupId == null){
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    private void validateCategoryGroupRegisterForm(CategoryGroupRegisterRequest request) {
        String name = request.getName();
        boolean isExist = categoryGroupRepository.existsByName(name);
        if(isExist == true){
            throw new CategoryPresentException(String.format("이미 입력하신 '%s'이란 카테고리 그룹명이 존재합니다", name));
        }
        validateName(name, "카테고리 그룹을 입력하세요.");
    }

    private static void validateName(String name, String exceptionMessage) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public void updateCategoryGroup(CategoryGroupUpdateRequest request, Long categoryGroupId) {
        validateCategoryGroupUpdateForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroup.updateCategoryGroup(request);
    }

    public void updateCategory(CategoryUpdateRequest request, Long categoryId){
        validateCategoryUpdateForm(request);

        Category category = findCategoryById(categoryId);

        Long categoryGroupId = request.getCategoryGroupId();

        if(categoryGroupId == null){
            category.updateCategory(request);
        }else {
            CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
            category.updateCategory(request, categoryGroup);
        }
    }

    private void validateCategoryGroupUpdateForm(CategoryGroupUpdateRequest request) {
        String name = request.getName();
        validateName(name, "카테고리 그룹을 입력하세요.");
    }
    
    private void validateCategoryUpdateForm(CategoryUpdateRequest request) {
        String name = request.getName();
        validateName(name, "카테고리를 입력하세요.");
    }
}
