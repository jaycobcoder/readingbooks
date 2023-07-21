package com.readingbooks.web.service.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Book findBook(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요."));
    }
}
