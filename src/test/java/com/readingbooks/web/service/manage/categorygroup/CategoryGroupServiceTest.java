package com.readingbooks.web.service.manage.categorygroup;

import com.readingbooks.web.domain.entity.category.CategoryGroup;
import com.readingbooks.web.exception.category.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryGroupServiceTest {
    @Autowired
    private CategoryGroupService categoryGroupService;

    @Test
    void whenRegisteringNameNullOrBlank_thenThrowException(){
        CategoryGroupRegisterRequest nullRequest = createCategoryGroupRequest(null);
        CategoryGroupRegisterRequest blankRequest = new CategoryGroupRegisterRequest("");

        assertThatThrownBy(()->categoryGroupService.register(blankRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(()->categoryGroupService.register(nullRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }

    @Test
    void whenCategoryGroupRegistered_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest request = createCategoryGroupRequest("소설");

        //when
        Long categoryGroupId = categoryGroupService.register(request);
        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);

        //then
        assertThat(categoryGroup.getId()).isEqualTo(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("소설");
    }

    @Test
    void whenUpdatingNameNullOrBlank_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryGroupUpdateRequest nullRequest = new CategoryGroupUpdateRequest(null);
        CategoryGroupUpdateRequest blankRequest = new CategoryGroupUpdateRequest("");

        assertThatThrownBy(() -> categoryGroupService.update(nullRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(() -> categoryGroupService.update(blankRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }

    @Test
    void whenUpdatingCategoryGroupIdNotFound_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        Long notFoundId = categoryGroupId + 1L;

        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");

        assertThatThrownBy(() -> categoryGroupService.update(request, notFoundId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenCategoryGroupUpdated_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        //when
        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");
        categoryGroupService.update(request, categoryGroupId);

        //then
        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("경제");
    }

    private static CategoryGroupRegisterRequest createCategoryGroupRequest(String name) {
        return new CategoryGroupRegisterRequest(name);
    }
}