package com.readingbooks.web.service.manage.category;

import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Test
    void whenRegisteringNameNullOrBlank_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest nullRequest = createCategoryRequest(null, categoryGroupId);
        CategoryRegisterRequest blankRequest = new CategoryRegisterRequest("", categoryGroupId);

        assertThatThrownBy(()-> categoryService.register(nullRequest))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("카테고리를 입력하세요.");
        assertThatThrownBy(()-> categoryService.register(blankRequest))
                        .hasMessageContaining("카테고리를 입력하세요.");
    }


    @Test
    void whenRegisteringCategoryGroupIdNull_thenThrowException(){
        CategoryRegisterRequest request = new CategoryRegisterRequest("판타지 소설", null);

        assertThatThrownBy(()-> categoryService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("카테고리 그룹 아이디를 입력하세요.");
    }

    @Test
    void whenRegisteringCategoryGroupIdNotFound_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        Long notFoundId = categoryGroupId + 1;

        CategoryRegisterRequest request = createCategoryRequest("판타지 소설", notFoundId);

        assertThatThrownBy(()-> categoryService.register(request))
                .isInstanceOf(CategoryNotFoundException.class)
                        .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenCategoryRegistered_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);

        //when
        Long categoryId = categoryService.register(categoryRequest);
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getId()).isEqualTo(categoryId);
        assertThat(category.getName()).isEqualTo("판타지 소설");
        assertThat(category.getCategoryGroup().getId()).isEqualTo(categoryGroupId);
        assertThat(category.getCategoryGroup().getName()).isEqualTo("소설");
    }

    @Test
    void whenUpdatingCategoryNameNullOrBlank_thenThrowException(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);

        CategoryUpdateRequest nullRequest = createCategoryUpdateRequest(null, categoryGroupId);
        CategoryUpdateRequest blankRequest = createCategoryUpdateRequest("", categoryGroupId);

        assertThatThrownBy(() -> categoryService.update(nullRequest, categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리를 입력하세요.");

        assertThatThrownBy(() -> categoryService.update(blankRequest, categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리를 입력하세요.");
    }

    @Test
    void whenUpdatingCategoryGroupIdNotFound_thenThrowException(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        Long notFoundCategoryGroupId = categoryGroupId + 1L;

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", notFoundCategoryGroupId);

        assertThatThrownBy(() -> categoryService.update(request, categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenUpdatingCategoryIdNotFound_thenThrowException(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);

        Long notFoundCategoryId = categoryId + 1L;

        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", categoryGroupId);

        assertThatThrownBy(() -> categoryService.update(request, notFoundCategoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenCategoryNameUpdated_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);

        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", categoryGroupId);

        //when
        categoryService.update(request, categoryId);
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getName()).isEqualTo("추리 소설");
    }

    @Test
    void whenCategoryNameAndCategoryGroupUpdated_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest novelRequest = createCategoryGroupRequest("소설");
        Long novelId = categoryGroupService.register(novelRequest);

        CategoryGroupRegisterRequest economicRequest = createCategoryGroupRequest("경제");
        Long economicId = categoryGroupService.register(economicRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", novelId);
        Long categoryId = categoryService.register(categoryRequest);

        CategoryUpdateRequest request = createCategoryUpdateRequest("경제 일반", economicId);

        //when
        categoryService.update(request, categoryId);
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getCategoryGroup().getName()).isEqualTo("경제");
        assertThat(category.getName()).isEqualTo("경제 일반");
    }

    private static CategoryUpdateRequest createCategoryUpdateRequest(String name, Long categoryGroupId) {
        return new CategoryUpdateRequest(name, categoryGroupId);
    }

    private static CategoryGroupRegisterRequest createCategoryGroupRequest(String name) {
        return new CategoryGroupRegisterRequest(name);
    }

    private static CategoryRegisterRequest createCategoryRequest(String name, Long categoryGroupId) {
        return new CategoryRegisterRequest(name, categoryGroupId);
    }
}