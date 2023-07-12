package com.readingbooks.web.service.manage.categorygroup;

import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.category.CategoryGroupRepository;
import com.readingbooks.web.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryGroupService {
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;

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
        validateUpdateForm(request, categoryGroupId);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroup.updateCategoryGroup(request);
    }

    private void validateUpdateForm(CategoryGroupUpdateRequest request, Long categoryGroupId) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);

        validateCategoryGroupId(categoryGroupId);
    }

    private void validateCategoryGroupId(Long categoryGroupId) {
        if(categoryGroupId == null){
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    /**
     * 카테고리 그룹 삭제 메소드
     * @param categoryGroupId
     * @return isDeleted
     */
    public boolean delete(Long categoryGroupId) {
        validateCategoryGroupId(categoryGroupId);

        boolean isCategoryExists = categoryRepository.existsByCategoryGroupId(categoryGroupId);

        if(isCategoryExists == true){
            throw new CategoryPresentException("해당 카테고리 그룹 아래 하위 카테고리들이 존재합니다. 하위 카테고리를 모두 삭제한 다음에 카테고리 그룹을 삭제해주세요.");
        }

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroupRepository.delete(categoryGroup);

        return true;
    }

    /**
     * 카테고리 그룹 이름으로 검색하여 DTO로 반환하는 메소드
     * @param name
     * @return CategoryGroupSearchResponse
     */
    @Transactional(readOnly = true)
    public CategoryGroupSearchResponse searchByCategoryName(String name) {
        Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByName(name);

        boolean isEmpty = categoryGroup.isEmpty();

        if(isEmpty){
            return new CategoryGroupSearchResponse(null, name, false);
        }

        return categoryGroup
                .map(c -> new CategoryGroupSearchResponse(c.getId(), c.getName(), true))
                .get();
    }
}
