package com.readingbooks.web.service.manage.bookauthorlist;

import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.service.manage.author.AuthorManagementService;
import com.readingbooks.web.service.manage.author.AuthorRegisterRequest;
import com.readingbooks.web.service.manage.book.BookManagementService;
import com.readingbooks.web.service.manage.book.BookRegisterRequest;
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
class BookAuthorListServiceTest {

    @Autowired
    private BookAuthorListService bookAuthorListService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private AuthorManagementService authorManagementService;

    @Test
    void whenBookIdNull_ThenThrowException(){
        //given
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);

        //when
        BookAuthorListRegisterRequest request = new BookAuthorListRegisterRequest(null, authorId, 1);
        assertThatThrownBy(() -> bookAuthorListService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 아이디를 입력해주세요.");
    }

    @Test
    void whenAuthorIdNull_ThenThrowException(){
        //given
        Long bookId = createBook();

        BookAuthorListRegisterRequest request = new BookAuthorListRegisterRequest(bookId, null, 1);
        assertThatThrownBy(() -> bookAuthorListService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작가 아이디를 입력해주세요.");
    }

    @Test
    void whenAuthorRegistered_thenVerifyRegistered(){
        Long bookId = createBook();
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);
        Long translatorId = createAuthor("유재석", AuthorOption.TRANSLATOR);
        Long illustratorId = createAuthor("박명수", AuthorOption.ILLUSTRATOR);

        BookAuthorListRegisterRequest authorRequest = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        BookAuthorListRegisterRequest translatorRequest = new BookAuthorListRegisterRequest(bookId, translatorId, 2);
        BookAuthorListRegisterRequest illustratorRequest = new BookAuthorListRegisterRequest(bookId, illustratorId, 3);

        bookAuthorListService.register(authorRequest);
        bookAuthorListService.register(translatorRequest);
        bookAuthorListService.register(illustratorRequest);
    }

    private static MockMultipartFile getMockMultipartFile() {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );
        return file;
    }


    private Long createAuthor(String name, AuthorOption option) {
        AuthorRegisterRequest authorRequest = createAuthorRegisterRequest(name, option, "테스트", "테스트입니다", "1919", Gender.SECRET);
        Long authorId = authorManagementService.registerAuthor(authorRequest);
        return authorId;
    }

    private Long createBook() {
        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);

        BookRegisterRequest bookRegisterRequest = createBookRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, null);

        MockMultipartFile file = getMockMultipartFile();

        return bookManagementService.registerBook(bookRegisterRequest, file);
    }

    private static BookRegisterRequest createBookRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId);
    }

    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }
}