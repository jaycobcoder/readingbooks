package com.readingbooks.web.service.manage.author;

import com.readingbooks.web.domain.entity.author.Author;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.repository.admin.author.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorManagementService {
    private final AuthorRepository authorRepository;

    public Long registerAuthor(AuthorRegisterRequest request) {
        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());
        Author author = Author.createAuthor(request);
        return authorRepository.save(author).getId();
    }

    private static void validateForm(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }

        if(name.length() < 2 || name.length() > 24){
            throw new IllegalArgumentException("이름을 올바르게 입력해주세요.");
        }

        if(option == null){
            throw new IllegalArgumentException("옵션을 선택해주세요.");
        }

        if(nationality == null || nationality.trim().equals("")){
            throw new IllegalArgumentException("국적을 입력해주세요.");
        }

        if(description == null || description.trim().equals("")){
            throw new IllegalArgumentException("설명을 입력해주세요.");
        }

        if(birthYear == null || birthYear.trim().equals("")){
            throw new IllegalArgumentException("생년을 입력해주세요.");
        }

        if(!(birthYear.length() == 4)){
            throw new IllegalArgumentException("생년을 올바르게 입력해주세요.");
        }

        birthYear = birthYear.substring(0, 2);
        if(!(birthYear.equals("19") || birthYear.equals("20"))){
            throw new IllegalArgumentException("생년을 올바르게 입력해주세요.");
        }

        if(gender == null){
            throw new IllegalArgumentException("성별을 입력해주세요.");
        }
    }

    public void updateAuthor(AuthorUpdateRequest request, Long authorId) {
        Author author = findAuthorById(authorId, "아이디로 작가를 찾을 수 없습니다.");

        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());

        author.updateAuthor(request);
    }

    public Author findAuthorById(Long authorId, String exceptionMessage) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotfoundException(exceptionMessage));
    }
}
