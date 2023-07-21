package com.readingbooks.web.service.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookAuthorList;
import com.readingbooks.web.domain.entity.book.BookGroup;
import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.bookauthorlist.BookAuthorListRepository;
import com.readingbooks.web.service.book.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookInformationService {

    private final BookRepository bookRepository;
    private final BookAuthorListRepository bookAuthorListRepository;

    public BookInformationResponse getBookInformation(String isbn) {
        Optional<Book> tempBook = bookRepository.getBookInformation(isbn);

        if(tempBook.isEmpty()){
            return null;
        }

        BookDto bookDto = tempBook
                .map(b -> new BookDto(b)).get();

        BookAuthorList authorInBook = bookAuthorListRepository.getMainAuthor(isbn);
        String author = authorInBook.getAuthor().getName();
        Long authorId = authorInBook.getAuthor().getId();

        int authorCountExceptMainAuthor = bookAuthorListRepository.getAuthorCount(isbn).intValue() - 1;

        BookAuthorList translatorInBook = bookAuthorListRepository.getMainTranslator(isbn);

        String translator = null;
        Long translatorId = null;
        if(translatorInBook != null){
            translatorInBook.getAuthor().getName();
            translatorInBook.getAuthor().getId();
        }

        AuthorDto authorDto = new AuthorDto(authorId, author, translatorId, authorCountExceptMainAuthor, translator);

        BookInformationResponse response = new BookInformationResponse(bookDto, authorDto);
        return response;
    }

    public List<BookGroupInformationResponse> getSeriesInformation(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        Optional<BookGroup> bookGroup = Optional.ofNullable(book.get().getBookGroup());

        if(bookGroup.isEmpty()){
            return new ArrayList<>();
        }

        return bookRepository.findByBookGroupId(bookGroup.get().getId()).stream()
                .map(b -> new BookGroupInformationResponse(b.getIsbn(), b.getTitle(), b.getSavedImageName()))
                .collect(Collectors.toList());
    }

    public List<AuthorNameAndIdResponse> getAuthorNameAndIdList(String isbn) {
        return bookAuthorListRepository.getAuthorNameAndIdList(isbn).stream()
                .map(bal -> new AuthorNameAndIdResponse(bal.getAuthor().getName(), bal.getAuthor().getId(), bal.getAuthor().getAuthorOption().toString()))
                .collect(Collectors.toList());
    }

    public AuthorInformationResponse getAuthorInformation(String isbn, Long authorId) {
        return bookAuthorListRepository.getAuthorInformation(isbn, authorId)
                .map(bal -> new AuthorInformationResponse(bal.getAuthor()))
                .orElseThrow(() -> new AuthorNotfoundException("작가를 찾을 수 없습니다."));
    }
}
