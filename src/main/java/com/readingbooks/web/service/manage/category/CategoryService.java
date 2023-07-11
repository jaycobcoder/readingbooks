package com.readingbooks.web.service.manage.category;

import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.category.CategoryRepository;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {
    private final CategoryGroupService categoryGroupService;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 등록 메소드
     * @param request (등록 DTO)
     * @return categoryId
     */
    public Long register(CategoryRegisterRequest request){
        Long categoryGroupId = request.getCategoryGroupId();
        String name = request.getName();

        validateRegisterForm(request);

        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);

        Category category = Category.createCategory(name, categoryGroup);
        return categoryRepository.save(category).getId();
    }

    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요."));
        return category;
    }

    private void validateRegisterForm(CategoryRegisterRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);

        Long categoryGroupId = request.getCategoryGroupId();
        validateCategoryGroupId(categoryGroupId);
    }

    private void validateNameExist(String name) {
        boolean isExist = categoryRepository.existsByName(name);
        if(isExist == true){
            throw new CategoryPresentException(String.format("이미 입력하신 '%s'이란 카테고리가 존재합니다", name));
        }
    }

    private void validateName(String name) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("카테고리를 입력하세요.");
        }
    }

    private void validateCategoryGroupId(Long categoryGroupId) {
        if(categoryGroupId == null){
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    /**
     * 카테고리 수정 메소드
     * @param request (업데이트 DTO)
     * @param categoryId
     */
    public void update(CategoryUpdateRequest request, Long categoryId){
        validateUpdateForm(request);

        Category category = findCategoryById(categoryId);

        Long categoryGroupId = request.getCategoryGroupId();
        validateCategoryGroupId(categoryGroupId);

        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);
        category.updateCategory(request, categoryGroup);
    }
    
    private void validateUpdateForm(CategoryUpdateRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);

        Long categoryGroupId = request.getCategoryGroupId();
        validateCategoryGroupId(categoryGroupId);
    }
}
