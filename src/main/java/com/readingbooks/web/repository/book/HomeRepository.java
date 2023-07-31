package com.readingbooks.web.repository.book;

import com.readingbooks.web.service.home.HomeBooksResponse;

import java.util.List;

public interface HomeRepository {
    List<HomeBooksResponse> findBestBooks();
    List<HomeBooksResponse> findNewbooks();
}
