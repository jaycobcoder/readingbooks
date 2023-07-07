package com.readingbooks.web.service.manage.category;

import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired CategoryService categoryService;

    @Test
    void category_group_register_fail_name_null(){
        CategoryGroupRegisterRequest nullRequest = createCategoryGroupRequest(null);
        CategoryGroupRegisterRequest blankRequest = new CategoryGroupRegisterRequest("");
        assertThatThrownBy(()->categoryService.registerCategoryGroup(blankRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(()->categoryService.registerCategoryGroup(nullRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }


    @Test
    void category_group_register_success(){
        //given
        CategoryGroupRegisterRequest request = createCategoryGroupRequest("소설");

        //when
        Long categoryGroupId = categoryService.registerCategoryGroup(request);
        CategoryGroup categoryGroup = categoryService.findCategoryGroupById(categoryGroupId);

        //then
        assertThat(categoryGroup.getId()).isEqualTo(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("소설");
    }

    @Test
    void category_register_fail_name_null(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);


        CategoryRegisterRequest nullRequest = createCategoryRequest(null, categoryGroupId);
        CategoryRegisterRequest blankRequest = new CategoryRegisterRequest("", categoryGroupId);

        assertThatThrownBy(()-> categoryService.registerCategory(nullRequest))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("카테고리를 입력하세요.");
        assertThatThrownBy(()-> categoryService.registerCategory(blankRequest))
                        .hasMessageContaining("카테고리를 입력하세요.");
    }


    @Test
    void category_register_fail_category_group_id_null(){
        CategoryRegisterRequest request = new CategoryRegisterRequest("판타지 소설", null);

        assertThatThrownBy(()-> categoryService.registerCategory(request))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("카테고리 그룹 아이디를 입력하세요.");
    }

    @Test
    void category_register_fail_category_group_id_not_found(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        Long notFoundId = categoryGroupId + 1;

        CategoryRegisterRequest request = createCategoryRequest("판타지 소설", notFoundId);

        assertThatThrownBy(()-> categoryService.registerCategory(request))
                .isInstanceOf(CategoryNotFoundException.class)
                        .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void category_register_success(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);

        //when
        Long categoryId = categoryService.registerCategory(categoryRequest);
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getId()).isEqualTo(categoryId);
        assertThat(category.getName()).isEqualTo("판타지 소설");
        assertThat(category.getCategoryGroup().getId()).isEqualTo(categoryGroupId);
        assertThat(category.getCategoryGroup().getName()).isEqualTo("소설");
    }

    @Test
    void category_group_update_fail_name_null(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        CategoryGroupUpdateRequest nullRequest = new CategoryGroupUpdateRequest(null);
        CategoryGroupUpdateRequest blankRequest = new CategoryGroupUpdateRequest("");

        assertThatThrownBy(() -> categoryService.updateCategoryGroup(nullRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(() -> categoryService.updateCategoryGroup(blankRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }

    @Test
    void category_group_update_fail_id_not_found(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        Long notFoundId = categoryGroupId + 1L;

        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");

        assertThatThrownBy(() -> categoryService.updateCategoryGroup(request, notFoundId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void category_group_update_success(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");
        categoryService.updateCategoryGroup(request, categoryGroupId);

        CategoryGroup categoryGroup = categoryService.findCategoryGroupById(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("경제");
    }


    @Test
    void category_update_fail_name_null(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.registerCategory(categoryRequest);

        CategoryUpdateRequest nullRequest = createCategoryUpdateRequest(null, categoryGroupId);
        CategoryUpdateRequest blankRequest = createCategoryUpdateRequest("", categoryGroupId);

        assertThatThrownBy(() -> categoryService.updateCategory(nullRequest, categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리를 입력하세요.");

        assertThatThrownBy(() -> categoryService.updateCategory(blankRequest, categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리를 입력하세요.");
    }



    @Test
    void category_update_fail_categoryGroupId_not_found(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        Long notFoundCategoryGroupId = categoryGroupId + 1L;

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.registerCategory(categoryRequest);


        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", notFoundCategoryGroupId);

        assertThatThrownBy(() -> categoryService.updateCategory(request, categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void category_update_fail_categoryId_not_found(){
        //카테고리 그룹 생성
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        //카테고리 생성
        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.registerCategory(categoryRequest);

        Long notFoundCategoryId = categoryId + 1L;

        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", null);

        assertThatThrownBy(() -> categoryService.updateCategory(request, notFoundCategoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요.");
    }

    @Test
    void category_update_success_case1_only_name_change(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.registerCategory(categoryRequest);

        CategoryUpdateRequest request = createCategoryUpdateRequest("추리 소설", null);

        //when
        categoryService.updateCategory(request, categoryId);
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getName()).isEqualTo("추리 소설");
    }

    @Test
    void category_update_success_case2_name_and_group(){
        //given
        CategoryGroupRegisterRequest novelRequest = createCategoryGroupRequest("소설");
        Long novelId = categoryService.registerCategoryGroup(novelRequest);

        CategoryGroupRegisterRequest economicRequest = createCategoryGroupRequest("경제");
        Long economicId = categoryService.registerCategoryGroup(economicRequest);

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", novelId);
        Long categoryId = categoryService.registerCategory(categoryRequest);

        CategoryUpdateRequest request = createCategoryUpdateRequest("경제 일반", economicId);

        //when
        categoryService.updateCategory(request, categoryId);
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