package com.readingbooks.web.repository.book;

import com.readingbooks.web.service.search.BookSearchCondition;
import com.readingbooks.web.service.search.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchBookRepository {
    Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition);
}
