package com.readingbooks.web.service.manage.bookcontent;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.bookcontent.BookContentPresentException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.bookcontent.BookContentRepository;
import com.readingbooks.web.service.manage.book.BookManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookContentService {

    private final BookManagementService bookManagementService;
    private final BookContentRepository bookContentRepository;

    /**
     * 도서 내용 등록 메소드
     * @param request
     * @return bookContentId
     */
    public Long register(BookContentRegisterRequest request) {
        Long bookId = request.getBookId();
        String content = request.getContent();

        boolean isRegisteredBookId = bookContentRepository.existsByBookId(bookId);
        if(isRegisteredBookId == true){
            throw new BookContentPresentException("이미 도서 내용이 등록된 도서 아이디입니다.");
        }

        validateForm(bookId, content);
        Book book = bookManagementService.findBook(bookId);
        BookContent bookContent = BookContent.createBookContent(book, content);

        return bookContentRepository.save(bookContent).getId();
    }

    private void validateForm(Long bookId, String content) {
        validateBookContentId(bookId);

        if(content == null || content.trim().equals("")){
            throw new IllegalArgumentException("도서의 내용을 입력해주세요.");
        }
    }

    private void validateBookContentId(Long bookId) {
        if(bookId == null){
            throw new IllegalArgumentException("도서 아이디를 입력해주세요.");
        }
    }

    /**
     * 도서 내용 수정 메소드
     * @param bookId
     * @param content
     */
    public void update(Long bookId, String content) {
        validateForm(bookId, content);

        BookContent bookContent = findBookContent(bookId);
        bookContent.updateContent(content);
    }

    @Transactional(readOnly = true)
    public BookContent findBookContent(Long bookId) {
        return bookContentRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요."));
    }

    /**
     * 도서 내용 제거 메소드
     * @param bookId
     */
    public void delete(Long bookId) {
        validateBookContentId(bookId);
        BookContent bookContent = findBookContent(bookId);
        bookContentRepository.delete(bookContent);
    }

    @Transactional(readOnly = true)
    public BookContentUpdateResponse searchBookContent(Long bookId) {
        Optional<BookContent> bookContent = bookContentRepository.findByBookId(bookId);

        if(bookContent.isEmpty()){
            return null;
        }
        return bookContent.map(b -> new BookContentUpdateResponse(b.getBook().getTitle(), b.getBook().getId(), b.getContent())).get();
    }
}
