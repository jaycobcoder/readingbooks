package com.readingbooks.web.service.manage.category;

import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.category.CategoryRepository;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {
    private final CategoryGroupService categoryGroupService;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    /**
     * 카테고리 등록 메소드
     * @param request (등록 DTO)
     * @return categoryId
     */
    public Long register(CategoryRegisterRequest request){
        Long categoryGroupId = request.getCategoryGroupId();
        String name = request.getName();

        validateRegisterForm(request);

        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroup(categoryGroupId);

        Category category = Category.createCategory(name, categoryGroup);
        return categoryRepository.save(category).getId();
    }

    @Transactional(readOnly = true)
    public Category findCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요."));
        return category;
    }

    private void validateRegisterForm(CategoryRegisterRequest request) {
        String name = request.getName();
        validateIsExistsName(name);
        validateName(name);

        Long categoryGroupId = request.getCategoryGroupId();
        validateCategoryGroupId(categoryGroupId);
    }

    private void validateIsExistsName(String name) {
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
        validateCategoryId(categoryId);

        Category category = findCategory(categoryId);

        category.updateCategory(request);
    }

    private void validateCategoryId(Long categoryId) {
        if(categoryId == null){
            throw new IllegalArgumentException("카테고리 아이디를 입력하세요.");
        }
    }

    private void validateUpdateForm(CategoryUpdateRequest request) {
        String name = request.getName();
        validateIsExistsName(name);
        validateName(name);
    }

    /**
     * 카테고리 삭제 메소드
     * @param categoryId
     * @return isDeleted
     */
    public boolean delete(Long categoryId){
        validateCategoryId(categoryId);

        boolean isCategoryExists = bookRepository.existsByCategoryId(categoryId);

        if(isCategoryExists == true){
            throw new CategoryPresentException("해당 카테고리를 설정한 도서가 존재합니다. 하위 도서를 모두 삭제한 다음에 카테고리를 삭제해주세요.");
        }

        Category category = findCategory(categoryId);
        categoryRepository.delete(category);
        return true;
    }

    /**
     * 카테고리 이름으로 검색하여 DTO로 반환하는 메소드
     * @param name
     * @return CategorySearchResponse
     */
    @Transactional(readOnly = true)
    public CategorySearchResponse searchCategory(String name) {
        Optional<Category> categoryGroup = categoryRepository.findByName(name);
        boolean isEmpty = categoryGroup.isEmpty();

        if(isEmpty){
            return new CategorySearchResponse(null, null, name, null, false);
        }

        return categoryGroup
                .map(c -> new CategorySearchResponse(c.getCategoryGroup().getId(), c.getId(), c.getName(), c.getCategoryGroup().getName(), true))
                .get();
    }

    /**
     * 모든 카테고리 조회 메소드
     * @return Dto List
     */
    @Transactional(readOnly = true)
    public List<CategorySearchResponse> searchCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(c -> new CategorySearchResponse(c.getCategoryGroup().getId(), c.getId(), c.getName(), c.getCategoryGroup().getName(), true))
                .collect(Collectors.toList());
    }
}