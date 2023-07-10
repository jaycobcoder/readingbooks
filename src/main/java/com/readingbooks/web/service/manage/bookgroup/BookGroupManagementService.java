package com.readingbooks.web.service.manage.bookgroup;

import com.readingbooks.web.domain.entity.book.BookGroup;
import com.readingbooks.web.exception.bookgroup.BookGroupNotFoundException;
import com.readingbooks.web.repository.bookgroup.BookGroupRepository;
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
public class BookGroupManagementService {

    private final BookGroupRepository bookGroupRepository;
    private final ImageUploadUtil imageUploadUtil;

    public Long registerBookGroup(BookGroupRegisterRequest request, MultipartFile file) {
        String title = request.getTitle();
        if(title == null || title.trim().equals("")){
            throw new IllegalArgumentException("도서 그룹명을 입력해주세요.");
        }

        String savedImageName = imageUploadUtil.uploadImage(file);

        BookGroup bookGroup = BookGroup.createBookGroup(request, savedImageName);
        return bookGroupRepository.save(bookGroup).getId();
    }

    public BookGroup findBookGroupById(Long bookGroupId){
        return bookGroupRepository.findById(bookGroupId)
                .orElseThrow(() -> new BookGroupNotFoundException("검색되는 도서 그룹이 없습니다. 도서 그룹 아이디를 다시 확인해주세요."));
    }

    public void updateBookGroupImage(MultipartFile file, Long bookGroupId) {
        BookGroup bookGroup = findBookGroupById(bookGroupId);
        String existingImageName = bookGroup.getSavedImageName();

        String updatedImageName = imageUploadUtil.updateImage(file, existingImageName);
        bookGroup.updateImage(updatedImageName);
    }

    public void updateBookGroupTitle(String updatedTitle, Long bookGroupId) {
        BookGroup bookGroup = findBookGroupById(bookGroupId);

        bookGroup.updateTitle(updatedTitle);
    }
}
