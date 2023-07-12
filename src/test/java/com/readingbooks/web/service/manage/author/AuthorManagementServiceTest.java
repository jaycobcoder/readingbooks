package com.readingbooks.web.service.manage.author;

import com.readingbooks.web.domain.entity.author.Author;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorManagementServiceTest {
    @Autowired
    private AuthorManagementService authorManagementService;

    @Test
    void whenRegisteringNameNull_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest(null, AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void whenRegisteringInvalidNameLength_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("김", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringNameBlank_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void whenRegisteringOptionNull_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("test", null, "대한민국", "test", "1999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("옵션을 선택해주세요.");
    }

    @Test
    void whenRegisteringBirthyearNull_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", null, Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 입력해주세요.");
    }

    @Test
    void whenRegisteringInvalidBirthyearLength_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "19999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void whenRegisteringBirthyearNotStartedWith19or20_thenThrowException(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1899", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void whenAuthorRegistered_thenVerifyFields(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

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

    public Long registerAuthor(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);
        return authorManagementService.register(request);
    }
    private AuthorRegisterRequest createRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }

    private static AuthorUpdateRequest createUpdateRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorUpdateRequest(name, option, nationality, description, birthYear, gender);
    }
}