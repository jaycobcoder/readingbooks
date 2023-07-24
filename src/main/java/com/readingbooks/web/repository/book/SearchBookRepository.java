package com.readingbooks.web.repository.book;

import com.readingbooks.web.service.search.BookSearchCondition;
import com.readingbooks.web.service.search.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBookRepository {
    Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition);
}
