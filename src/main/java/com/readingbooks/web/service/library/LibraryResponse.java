package com.readingbooks.web.service.library;

import com.readingbooks.web.domain.entity.library.Library;
import lombok.Getter;

@Getter
public class LibraryResponse {
    private Long bookId;
    private String title;
    private String savedImageName;

    public LibraryResponse(Library library) {
        this.bookId = library.getBook().getId();
        this.title = library.getBook().getTitle();
        this.savedImageName = library.getBook().getSavedImageName();
    }
}
