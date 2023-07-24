package com.readingbooks.web.service.manage.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookAuthorList;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.author.AuthorPresentException;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.bookcontent.BookContentPresentException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.bookauthorlist.BookAuthorListRepository;
import com.readingbooks.web.repository.bookcontent.BookContentRepository;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.manage.author.AuthorManagementService;
import com.readingbooks.web.service.manage.author.AuthorRegisterRequest;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListRegisterRequest;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListService;
import com.readingbooks.web.service.manage.bookcontent.BookContentRegisterRequest;
import com.readingbooks.web.service.manage.bookcontent.BookContentService;
import com.readingbooks.web.service.manage.bookgroup.BookGroupManagementService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.utils.ImageUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BookManagementServiceTest {
    private ImageUploadUtil imageUploadUtil;
    private BookManagementService bookManagementService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookGroupManagementService bookGroupManagementService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookContentRepository bookContentRepository;
    @Autowired
    private BookAuthorListRepository bookAuthorListRepository;
    @Autowired
    private CategoryGroupService categoryGroupService;
    @Autowired
    private AuthorManagementService authorManagementService;
    @Autowired
    private BookContentService bookContentService;
    @Autowired
    private BookAuthorListService bookAuthorListService;
    @Autowired
    private BookService bookService;

    @BeforeEach
    void beforeEach(){
        imageUploadUtil = Mockito.mock(ImageUploadUtil.class);
        bookManagementService = new BookManagementService(bookService, categoryService, bookGroupManagementService, imageUploadUtil, bookRepository, bookContentRepository, bookAuthorListRepository);
    }

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
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long bookId = bookManagementService.register(request, file);

        Book book = bookService.findBook(bookId);
        assertThat(book.getTitle()).isEqualTo("해리포터와 마법사의 돌");
        assertThat(book.getIsbn()).isEqualTo("123123");
        assertThat(book.getPublisher()).isEqualTo("포터모어");
        assertThat(book.getPublishingDate()).isEqualTo("2023.01.01");
        assertThat(book.getPaperPrice()).isEqualTo(0);
        assertThat(book.getEbookPrice()).isEqualTo(9900);
        assertThat(book.getDiscountRate()).isEqualTo(5);
        assertThat(book.getCategory().getId()).isEqualTo(categoryId);
        assertThat(book.getBookGroup()).isNull();
        assertThat(book.getDescription()).isEqualTo("21세기 최고의 책");
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
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long bookId = bookManagementService.register(request, file);

        BookUpdateRequest updateRequest = new BookUpdateRequest("홍길동전", "test", "test", "test", 1, 1, 1, categoryId, 0L, "21세기 최고의 책");
        bookManagementService.update(updateRequest, bookId);

        Book book = bookService.findBook(bookId);

        assertThat(book.getTitle()).isEqualTo("홍길동전");
    }

    @Test
    void whenDeletingBookHasBookContent_thenThrowException(){
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
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");
        Long bookId = bookManagementService.register(request, file);

        BookContentRegisterRequest bookContentRegisterRequest = new BookContentRegisterRequest(bookId, "test");
        bookContentService.register(bookContentRegisterRequest);

        //when
        assertThatThrownBy(() -> bookManagementService.delete(bookId))
                .isInstanceOf(BookContentPresentException.class)
                .hasMessageContaining("해당 도서에는 도서 내용이 있습니다. 도서 내용을 삭제한 다음에 도서를 삭제해주세요.");
    }

    @Test
    void whenDeletingBookExistsAuthor_thenThrowException(){
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
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");
        Long bookId = bookManagementService.register(request, file);

        AuthorRegisterRequest authorRegisterRequest = new AuthorRegisterRequest(
                "test", AuthorOption.ILLUSTRATOR, "영국", "test", "1999", Gender.SECRET
        );
        Long authorId = authorManagementService.register(authorRegisterRequest);

        BookAuthorListRegisterRequest bookAuthorListRegisterRequest = new BookAuthorListRegisterRequest(
                bookId, authorId, 1
        );
        bookAuthorListService.register(bookAuthorListRegisterRequest);

        //when
        assertThatThrownBy(() -> bookManagementService.delete(bookId))
                .isInstanceOf(AuthorPresentException.class)
                .hasMessageContaining("해당 도서에 등록된 작가나 번역가 또는 삽화가가 있습니다. 이들을 삭제한 다음에 도서를 삭제해주세요.");
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
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");
        Long bookId = bookManagementService.register(request, file);

        //when
        boolean isDeleted = bookManagementService.delete(bookId);

        //then
        assertThat(isDeleted).isTrue();
        assertThatThrownBy(() -> bookService.findBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요.");
    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }
}