package com.readingbooks.web.domain.entity.book;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.service.manage.bookgroup.BookGroupRegisterRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_group_id")
    private Long id;
    private String title;
    private String savedImageName;

    public static BookGroup createBookGroup(BookGroupRegisterRequest request, String savedImageName) {
        BookGroup bookGroup = new BookGroup();
        bookGroup.title = request.getTitle();
        bookGroup.savedImageName = savedImageName;
        return bookGroup;
    }

    public void updateImage(String updatedImageName) {
        savedImageName = updatedImageName;
    }

    public void updateTitle(String updatedTitle) {
        title = updatedTitle;
    }
}
