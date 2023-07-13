package com.readingbooks.web.service.manage.bookgroup;

import com.readingbooks.web.domain.entity.book.BookGroup;
import com.readingbooks.web.exception.bookgroup.BookGroupNotFoundException;
import com.readingbooks.web.exception.category.CategoryPresentException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.bookgroup.BookGroupRepository;
import com.readingbooks.web.service.utils.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookGroupManagementService {

    private final BookGroupRepository bookGroupRepository;
    private final BookRepository bookRepository;
    private final ImageUploadUtil imageUploadUtil;

    /**
     * 도서 그룹 등록 메소드
     * @param request
     * @param file
     * @return BookGroupId
     */
    public Long register(BookGroupRegisterRequest request, MultipartFile file) {
        String title = request.getTitle();
        validateTitle(title);

        String savedImageName = imageUploadUtil.upload(file);

        BookGroup bookGroup = BookGroup.createBookGroup(request, savedImageName);
        return bookGroupRepository.save(bookGroup).getId();
    }

    private void validateTitle(String title) {
        if(title == null || title.trim().equals("")){
            throw new IllegalArgumentException("도서 그룹명을 입력해주세요.");
        }
    }

    @Transactional(readOnly = true)
    public BookGroup findBookGroup(Long bookGroupId){
        return bookGroupRepository.findById(bookGroupId)
                .orElseThrow(() -> new BookGroupNotFoundException("검색되는 도서 그룹이 없습니다. 도서 그룹 아이디를 다시 확인해주세요."));
    }

    /**
     * 도서 그룹 이미지 수정 메소드
     * @param file
     * @param bookGroupId
     */
    public void update(MultipartFile file, Long bookGroupId) {
        validateBookGroupId(bookGroupId);

        BookGroup bookGroup = findBookGroup(bookGroupId);
        String existingImageName = bookGroup.getSavedImageName();

        String updatedImageName = imageUploadUtil.update(file, existingImageName);
        bookGroup.updateImage(updatedImageName);
    }

    private void validateBookGroupId(Long bookGroupId) {
        if(bookGroupId == null){
            throw new IllegalArgumentException("도서 그룹 아이디를 입력해주세요");
        }
    }

    /**
     * 도서 그룹명 수정 메소드
     * @param updatedTitle
     * @param bookGroupId
     */
    public void update(String updatedTitle, Long bookGroupId) {
        validateTitle(updatedTitle);
        validateBookGroupId(bookGroupId);

        BookGroup bookGroup = findBookGroup(bookGroupId);

        bookGroup.updateTitle(updatedTitle);
    }

    /**
     * 도서 그룹 조회 메소드
     * @param title
     * @return
     */
    @Transactional(readOnly = true)
    public List<BookGroupSearchResponse> searchBookGroup(String title) {
        List<BookGroup> bookGroups = bookGroupRepository.findByTitle(title);
        return bookGroups.stream()
                .map(b -> new BookGroupSearchResponse(b.getId(), b.getTitle(), b.getSavedImageName()))
                .collect(Collectors.toList());
    }

    /**
     * 도서 그룹 삭제 메소드
     * @param bookGroupId
     * @return isDeleted
     */
    public boolean delete(Long bookGroupId) {
        validateBookGroupId(bookGroupId);

        boolean hasBooks = bookRepository.existsByBookGroupId(bookGroupId);

        if(hasBooks == true){
            throw new CategoryPresentException("해당 도서 그룹을 지정한 하위 도서가 존재합니다. 하위 도서를 모두 삭제한 다음에 도서 그룹을 삭제해주세요.");
        }

        BookGroup bookGroup = findBookGroup(bookGroupId);
        String savedImageName = bookGroup.getSavedImageName();
        bookGroupRepository.delete(bookGroup);
        imageUploadUtil.delete(savedImageName);
        return true;
    }
}
