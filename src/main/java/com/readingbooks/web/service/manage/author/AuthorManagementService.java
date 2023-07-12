package com.readingbooks.web.service.manage.author;

import com.readingbooks.web.domain.entity.author.Author;
import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.admin.author.AuthorRepository;
import com.readingbooks.web.repository.bookauthorlist.BookAuthorListRepository;
import com.readingbooks.web.service.manage.bookauthorlist.BookAuthorListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorManagementService {
    private final AuthorRepository authorRepository;
    private final BookAuthorListRepository bookAuthorListRepository;

    /**
     * 작가 등록 메소드
     * @param request (등록 DTO)
     * @return authorId
     */
    public Long register(AuthorRegisterRequest request) {
        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());
        Author author = Author.createAuthor(request);
        return authorRepository.save(author).getId();
    }

    private void validateForm(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
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

    /**
     * 작가 수정 메소드
     * @param request
     * @param authorId
     */
    public void updateAuthor(AuthorUpdateRequest request, Long authorId) {
        validateAuthorId(authorId);

        Author author = findAuthorById(authorId);

        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());

        author.updateAuthor(request);
    }

    public Author findAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotfoundException("아이디로 작가를 찾을 수 없습니다."));
    }

    /**
     * 작가 검색 메소드
     * @param name
     * @return List<AuthorSearchResponse> (DTO List)
     */
    @Transactional(readOnly = true)
    public List<AuthorSearchResponse> searchByAuthorName(String name) {
        List<Author> authors = authorRepository.findAllByName(name);

        return authors.stream()
                .map(a -> new AuthorSearchResponse(a.getId(), a.getName(), a.getBirthYear(), a.getGender().getKorean(), a.getAuthorOption().getKorean()))
                .collect(Collectors.toList());
    }

    /**
     * 작가 삭제 메소드
     * @param authorId
     * @return boolean
     */
    public boolean delete(Long authorId) {
        validateAuthorId(authorId);

        boolean isWrittenBooks = bookAuthorListRepository.existsByAuthorId(authorId);

        if(isWrittenBooks == true){
            throw new BookPresentException("해당 인물에 도서가 등록되었습니다. 하위 도서를 모두 삭제한 다음에 인물을 삭제해주세요.");
        }

        Author author = findAuthorById(authorId);
        authorRepository.delete(author);
        return true;
    }

    private void validateAuthorId(Long authorId) {
        if(authorId == null){
            throw new IllegalArgumentException("작가 아이디를 입력해주세요.");
        }
    }
}