package com.readingbooks.web.service.manage.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.service.manage.bookgroup.BookGroupManagementService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BookManagementServiceTest {

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private BookGroupManagementService bookGroupManagementService;

    @Test
    void whenBookRegistered_thenVerifyIsRegistered(){
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L);

        Long bookId = bookManagementService.register(request, file);

        Book book = bookManagementService.findBook(bookId);
        assertThat(book.getTitle()).isEqualTo("해리포터와 마법사의 돌");
        assertThat(book.getIsbn()).isEqualTo("123123");
        assertThat(book.getPublisher()).isEqualTo("포터모어");
        assertThat(book.getPublishingDate()).isEqualTo("2023.01.01");
        assertThat(book.getPaperPrice()).isEqualTo(0);
        assertThat(book.getEbookPrice()).isEqualTo(9900);
        assertThat(book.getDiscountRate()).isEqualTo(5);
        assertThat(book.getCategory().getId()).isEqualTo(categoryId);
        assertThat(book.getBookGroup()).isNull();
    }

    @Test
    void whenBookUpdatedContent_thenVerifyFields(){
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L);

        Long bookId = bookManagementService.register(request, file);

        BookUpdateRequest updateRequest = new BookUpdateRequest("홍길동전", "test", "test", "test", 1, 1, 1, categoryId, 0L);
        bookManagementService.update(updateRequest, bookId);

        Book book = bookManagementService.findBook(bookId);

        assertThat(book.getTitle()).isEqualTo("홍길동전");
    }

    @Test
    void whenBookDeleted_thenVerifyIsDeleted(){
        //given
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L);
        Long bookId = bookManagementService.register(request, file);

        //when
        boolean isDeleted = bookManagementService.delete(bookId);

        //then
        assertThat(isDeleted).isTrue();
        assertThatThrownBy(() -> bookManagementService.findBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요.");
    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId);
    }
}