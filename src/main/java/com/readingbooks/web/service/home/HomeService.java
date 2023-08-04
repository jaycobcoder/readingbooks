package com.readingbooks.web.service.home;

import com.readingbooks.web.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HomeService {
    private final BookRepository bookRepository;

    public List<HomeBooksResponse> findBestBooks() {
        return bookRepository.findBestBooks();
    }

    public List<HomeBooksResponse> findNewBooks() {
        return bookRepository.findNewbooks();
    }
}
