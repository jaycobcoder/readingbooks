package com.readingbooks.web.service.manage.bookcontent;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.repository.bookcontent.BookContentRepository;
import com.readingbooks.web.service.manage.book.BookManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookContentService {

    private final BookContentRepository bookContentRepository;
    private final BookManagementService bookManagementService;

    public Long register(BookContentRegisterRequest request) {
        Long bookId = request.getBookId();
        String content = request.getContent();

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

    public void delete(Long bookId) {
        validateBookContentId(bookId);
        BookContent bookContent = findBookContent(bookId);
        bookContentRepository.delete(bookContent);
    }
}
