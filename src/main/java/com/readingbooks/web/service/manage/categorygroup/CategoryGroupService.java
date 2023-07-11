package com.readingbooks.web.service.manage.categorygroup;

import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.category.CategoryGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryGroupService {
    private final CategoryGroupRepository categoryGroupRepository;

    /**
     * 카테고리 그룹 등록 메소드
     * @param request (등록 DTO)
     * @return categoryGroupId
     */
    public Long register(CategoryGroupRegisterRequest request) {
        validateRegisterForm(request);
        CategoryGroup categoryGroup = CategoryGroup.createCategoryGroup(request);
        return categoryGroupRepository.save(categoryGroup).getId();
    }

    public CategoryGroup findCategoryGroupById(Long categoryGroupId) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId)
                .orElseThrow(() -> new CategoryNotFoundException("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요."));
        return categoryGroup;
    }

    private void validateRegisterForm(CategoryGroupRegisterRequest request) {
        String name = request.getName();

        validateNameExist(name);
        validateName(name);
    }

    private void validateNameExist(String name) {
        boolean isExist = categoryGroupRepository.existsByName(name);
        if(isExist == true){
            throw new CategoryPresentException(String.format("이미 입력하신 '%s'이란 카테고리 그룹명이 존재합니다", name));
        }
    }

    private void validateName(String name) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("카테고리 그룹을 입력하세요.");
        }
    }

    /**
     * 카테고리 그룹 수정 메소드
     * @param request (업데이트 DTO)
     * @param categoryGroupId
     */
    public void update(CategoryGroupUpdateRequest request, Long categoryGroupId) {
        validateUpdateForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroup.updateCategoryGroup(request);
    }

    private void validateUpdateForm(CategoryGroupUpdateRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);
    }

    public void deleteCategoryGroup(Long categoryGroupId) {
    }
}
