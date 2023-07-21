package com.readingbooks.web.controller.book;

import com.readingbooks.web.exception.author.AuthorNotfoundException;
import com.readingbooks.web.service.book.*;
import com.readingbooks.web.service.book.dto.AuthorInformationResponse;
import com.readingbooks.web.service.book.dto.AuthorNameAndIdResponse;
import com.readingbooks.web.service.book.dto.BookGroupInformationResponse;
import com.readingbooks.web.service.book.dto.BookInformationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookInformationService bookInformationService;

    @GetMapping("/book/{isbn}")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String book(@PathVariable String isbn, HttpServletResponse response, Model model) throws IOException {
        BookInformationResponse bookInformation = bookInformationService.getBookInformation(isbn);

        if(bookInformation == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "/error/404";
        }

        List<BookGroupInformationResponse> seriesInformation = bookInformationService.getSeriesInformation(isbn);
        List<AuthorNameAndIdResponse> authorNameAndIdList = bookInformationService.getAuthorNameAndIdList(isbn);

        AuthorInformationResponse authorInformation = null;
        Long authorId = bookInformation.getAuthorDto().getAuthorId();

        try{
            authorInformation = bookInformationService.getAuthorInformation(isbn, authorId);
        } catch (AuthorNotfoundException e){
            authorInformation = null;
        }

        model.addAttribute("information", bookInformation);
        model.addAttribute("booksInGroup", seriesInformation);
        model.addAttribute("authorNameAndIdList", authorNameAndIdList);
        model.addAttribute("authorInformation", authorInformation);
        return "book/book";
    }

    @GetMapping("/book/{isbn}/{authorId}")
    @ResponseBody
    public ResponseEntity<Object> getAuthorInformation(@PathVariable String isbn, @PathVariable Long authorId){
        AuthorInformationResponse authorInformation = bookInformationService.getAuthorInformation(isbn, authorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(authorInformation);
    }
}
