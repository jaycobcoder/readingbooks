package com.readingbooks.web.service.manage.book;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.book.BookGroup;
import com.readingbooks.web.domain.entity.category.Category;
import com.readingbooks.web.exception.book.BookNotFoundException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.service.manage.bookgroup.BookGroupManagementService;
import com.readingbooks.web.service.manage.category.CategoryService;
import com.readingbooks.web.service.utils.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookManagementService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final ImageUploadUtil imageUploadUtil;
    private final BookGroupManagementService bookGroupManagementService;

    public Long registerBook(BookRegisterRequest request, MultipartFile file) {

        validateForm(
                request.getTitle(), request.getIsbn(), request.getPublisher(),
                request.getPublishingDate(), request.getEbookPrice(), request.getCategoryId(),
                request.getBookGroupId()
        );

        String savedImageName = imageUploadUtil.uploadImage(file);

        Category category = getCategory(request.getCategoryId());
        BookGroup bookGroup = getBookGroup(request.getBookGroupId());
        Book book = Book.createBook(request, category, bookGroup, savedImageName);

        return bookRepository.save(book).getId();
    }

    public Book findBookById(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요."));
    }

    private Category getCategory(Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        return category;
    }

    private BookGroup getBookGroup(Long bookGroupId) {
        BookGroup bookGroup = null;
        if(bookGroupId != null){
            bookGroup = bookGroupManagementService.findBookGroupById(bookGroupId);
        }
        return bookGroup;
    }

    private void validateForm(String title, String isbn, String publisher, String publishingDate,
                              int ebookPrice, Long categoryId, Long bookGroupId) {

        if(title.trim().equals("") || title == null){
            throw new IllegalArgumentException("제목을 입력해주세요");
        }

        if(isbn.trim().equals("") || isbn == null){
            throw new IllegalArgumentException("isbn을 입력해주세요");
        }

        if(publisher.trim().equals("") || publisher == null){
            throw new IllegalArgumentException("출판사를 입력해주세요");
        }

        if(publishingDate.trim().equals("") || publishingDate == null){
            throw new IllegalArgumentException("출판일을 입력해주세요");
        }

        if(ebookPrice == 0){
            throw new IllegalArgumentException("e-book 판매 가격을 입력해주세요");
        }

        if(categoryId == null){
            throw new IllegalArgumentException("카테고리 아이디를 입력해주세요");
        }
    }

    public void updateBookImage(MultipartFile file, Long bookId) {
        Book book = findBookById(bookId);
        String existingImageName = book.getSavedImageName();
        String updatedImageName = imageUploadUtil.updateImage(file, existingImageName);

        book.updateImage(updatedImageName);
    }

    public void updateBookContent(BookUpdateRequest request, Long bookId) {
        validateForm(
                request.getTitle(), request.getIsbn(), request.getPublisher(),
                request.getPublishingDate(), request.getEbookPrice(), request.getCategoryId(),
                request.getBookGroupId()
        );

        Book book = findBookById(bookId);

        Category category = getCategory(request.getCategoryId());
        BookGroup bookGroup = getBookGroup(request.getBookGroupId());

        book.updateContent(request, category, bookGroup);
    }
}
