package com.readingbooks.web.service.manage.author;

import com.readingbooks.web.domain.entity.author.Author;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.service.manage.book.BookManagementService;
import com.readingbooks.web.service.manage.book.BookRegisterRequest;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListRegisterRequest;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListService;
import com.readingbooks.web.service.manage.category.CategoryRegisterRequest;
import com.readingbooks.web.service.manage.category.CategoryService;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupRegisterRequest;
import com.readingbooks.web.service.manage.categorygroup.CategoryGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorManagementServiceTest {
    @Autowired
    private AuthorManagementService authorManagementService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private BookAuthorListService authorListService;

    @Test
    void whenRegisteringNameNull_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest(null, AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void whenRegisteringInvalidNameLength_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("김", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringNameBlank_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void whenRegisteringOptionNull_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", null, "대한민국", "test", "1999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("옵션을 선택해주세요.");
    }

    @Test
    void whenRegisteringBirthyearNull_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", null, Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 입력해주세요.");
    }

    @Test
    void whenRegisteringInvalidBirthyearLength_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "19999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringBirthyearNotStartedWith19or20_thenThrowException(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1899", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void whenAuthorRegistered_thenVerifyFields(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);
        Long authorId = authorManagementService.register(request);

        Author findAuthor = authorManagementService.findAuthorById(authorId);

        assertThat(findAuthor.getName()).isEqualTo("test");
        assertThat(findAuthor.getAuthorOption()).isEqualTo(AuthorOption.AUTHOR);
        assertThat(findAuthor.getNationality()).isEqualTo("대한민국");
        assertThat(findAuthor.getDescription()).isEqualTo("test");
        assertThat(findAuthor.getBirthYear()).isEqualTo("1999");
        assertThat(findAuthor.getGender()).isEqualTo(Gender.MEN);
    }

    @Test
    void whenUpdatingFormNull_thenThrowException(){
        Long authorId = registerAuthor();

        AuthorUpdateRequest request = createUpdateRequest(null, null, null, null, null, null);

        assertThatThrownBy(() -> authorManagementService.updateAuthor(request, authorId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenUpdatingAuthorIdNotFound_thenThrowException(){
        Long authorId = registerAuthor();

        AuthorUpdateRequest request = createUpdateRequest("test", AuthorOption.AUTHOR, "대한민국", "test" , "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.updateAuthor(request, authorId + 1L))
                .isInstanceOf(AuthorNotfoundException.class)
                .hasMessageContaining("아이디로 작가를 찾을 수 없습니다.");
    }

    @Test
    void whenAuthorUpdated_thenVerifyFields(){
        //given
        Long authorId = registerAuthor();
        AuthorUpdateRequest request = createUpdateRequest("updateTest", AuthorOption.TRANSLATOR, "미국", "hello", "1999", Gender.MEN);

        //when
        authorManagementService.updateAuthor(request, authorId);

        //then
        Author findAuthor = authorManagementService.findAuthorById(authorId);
        assertThat(findAuthor.getName()).isEqualTo("updateTest");
        assertThat(findAuthor.getAuthorOption()).isEqualTo(AuthorOption.TRANSLATOR);
        assertThat(findAuthor.getNationality()).isEqualTo("미국");
        assertThat(findAuthor.getDescription()).isEqualTo("hello");
    }

    @Test
    void whenAuthorSearched_thenVerifyIsSearched(){
        //given
        AuthorRegisterRequest firstRequest = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        authorManagementService.register(firstRequest);
        AuthorRegisterRequest secondRequest = createAuthorRegisterRequest("test", AuthorOption.TRANSLATOR, "대한민국", "test", "1999", Gender.MEN);
        authorManagementService.register(secondRequest);

        //when
        List<AuthorSearchResponse> authors = authorManagementService.searchByAuthorName("test");

        //given
        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors.get(0).getAuthorOption()).isEqualTo(AuthorOption.AUTHOR.getKorean());
        assertThat(authors.get(1).getAuthorOption()).isEqualTo(AuthorOption.TRANSLATOR.getKorean());
    }

    @Test
    void whenDeletingAuthorExistsBook_thenThrowException(){
        //given
        AuthorRegisterRequest firstRequest = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        Long authorId = authorManagementService.register(firstRequest);

        Long bookId = registerBook();

        BookAuthorListRegisterRequest authorRequest = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        authorListService.register(authorRequest);

        //when
        assertThatThrownBy(() -> authorManagementService.delete(authorId))
                .isInstanceOf(BookPresentException.class)
                .hasMessageContaining("해당 인물에 도서가 등록되었습니다. 하위 도서를 모두 삭제한 다음에 인물을 삭제해주세요.");
    }

    private Long registerBook() {
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


        BookRegisterRequest request = createBookRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, null);

        return bookManagementService.registerBook(request, file);
    }

    public Long registerAuthor(){
        AuthorRegisterRequest request = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);
        return authorManagementService.register(request);
    }
    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }

    private static AuthorUpdateRequest createUpdateRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorUpdateRequest(name, option, nationality, description, birthYear, gender);
    }

    private static BookRegisterRequest createBookRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId);
    }
}