package com.readingbooks.web.service.manage.bookauthorlist;

import com.readingbooks.web.domain.entity.author.Author;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookAuthorList;
import com.readingbooks.web.exception.bookauthorlist.BookAuthorListNotFoundException;
import com.readingbooks.web.repository.bookauthorlist.BookAuthorListRepository;
import com.readingbooks.web.service.manage.author.AuthorManagementService;
import com.readingbooks.web.service.manage.book.BookManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookAuthorListService {
    private final BookAuthorListRepository bookAuthorListRepository;
    private final AuthorManagementService authorManagementService;
    private final BookManagementService bookManagementService;

    public Long register(BookAuthorListRegisterRequest request) {
        Long bookId = request.getBookId();
        Long authorId = request.getAuthorId();
        int ordinal = request.getOrdinal();

        validateForm(bookId, authorId, ordinal);

        Book book = bookManagementService.findBookById(bookId);
        Author author = authorManagementService.findAuthorById(authorId);

        BookAuthorList bookAuthorList = BookAuthorList.createBookAuthorList(book, author, ordinal);

        return bookAuthorListRepository.save(bookAuthorList).getId();
    }

    private static void validateForm(Long bookId, Long authorId, int ordinal) {
        validateId(bookId, authorId);

        if(ordinal == 0){
            throw new IllegalArgumentException("서수를 입력해주세요.");
        }

    }

    private static void validateId(Long bookId, Long authorId) {
        if(bookId == null){
            throw new IllegalArgumentException("도서 아이디를 입력해주세요.");
        }

        if(authorId == null){
            throw new IllegalArgumentException("작가 아이디를 입력해주세요.");
        }
    }

    public void delete(Long bookId, Long authorId) {
        validateId(bookId, authorId);

        BookAuthorList bookAuthorList = findBookAuthorListByBookIdAndAuthorId(bookId, authorId);

        bookAuthorListRepository.delete(bookAuthorList);
    }

    public BookAuthorList findBookAuthorListByBookIdAndAuthorId(Long bookId, Long authorId) {
        BookAuthorList bookAuthorList = bookAuthorListRepository.findByBookIdAndAuthorId(bookId, authorId)
                .orElseThrow(() -> new BookAuthorListNotFoundException("검색되는 결과가 없습니다. 도서 아이디와 작가 아이디를 다시 확인해주세요."));
        return bookAuthorList;
    }
}
