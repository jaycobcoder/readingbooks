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
    void register_fail_name_null(){
        AuthorRegisterRequest request = createRegisterRequest(null, AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void register_fail_name_length(){
        AuthorRegisterRequest request = createRegisterRequest("김", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_name_blank(){
        AuthorRegisterRequest request = createRegisterRequest("", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름을 입력해주세요.");
    }

    @Test
    void register_fail_option_null(){
        AuthorRegisterRequest request = createRegisterRequest("test", null, "대한민국", "test", "1999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("옵션을 선택해주세요.");
    }

    @Test
    void register_fail_birthYear_null(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", null, Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 입력해주세요.");
    }

    @Test
    void register_fail_birthYear_length(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "19999", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_birthYear_not_start_19or20(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1899", Gender.MEN);
        assertThatThrownBy(() -> authorManagementService.registerAuthor(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void register_success(){
        AuthorRegisterRequest request = createRegisterRequest("test", AuthorOption.AUTHOR, "대한민국", "test", "1999", Gender.MEN);

        Long authorId = authorManagementService.registerAuthor(request);
        Author findAuthor = authorManagementService.findAuthorById(authorId);

        assertThat(findAuthor.getName()).isEqualTo("test");
        assertThat(findAuthor.getAuthorOption()).isEqualTo(AuthorOption.AUTHOR);
        assertThat(findAuthor.getNationality()).isEqualTo("대한민국");
        assertThat(findAuthor.getDescription()).isEqualTo("test");

    }

    @Test
    void update_fail_form_null(){
        Long authorId = registerAuthor();

        AuthorUpdateRequest request = createUpdateRequest(null, null, null, null, null, null);

        assertThatThrownBy(() -> authorManagementService.updateAuthor(request, authorId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_fail_id_not_found(){
        Long authorId = registerAuthor();

        AuthorUpdateRequest request = createUpdateRequest("test", AuthorOption.AUTHOR, "대한민국", "test" , "1999", Gender.MEN);

        assertThatThrownBy(() -> authorManagementService.updateAuthor(request, authorId + 1L))
                .isInstanceOf(AuthorNotfoundException.class)
                .hasMessageContaining("아이디로 작가를 찾을 수 없습니다.");
    }

    @Test
    void update_success(){
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
        return authorManagementService.registerAuthor(request);
    }
    private AuthorRegisterRequest createRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }

    private static AuthorUpdateRequest createUpdateRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorUpdateRequest(name, option, nationality, description, birthYear, gender);
    }
}