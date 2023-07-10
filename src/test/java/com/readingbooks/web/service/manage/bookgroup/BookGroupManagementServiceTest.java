package com.readingbooks.web.service.manage.bookgroup;

import com.readingbooks.web.domain.entity.book.BookGroup;
import com.readingbooks.web.repository.bookgroup.BookGroupRepository;
import com.readingbooks.web.service.utils.ImageUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Transactional
@SpringBootTest
class BookGroupManagementServiceTest {
    @Autowired
    private BookGroupRepository bookGroupRepository;
    private BookGroupManagementService bookGroupManagementService;
    private ImageUploadUtil imageUploadUtil;

    @BeforeEach
    void beforeEach(){
        bookGroupManagementService = getBookGroupManagementService();
    }

    @Test
    void whenTitleIsNullOrBlank_thenThrowException(){
        MockMultipartFile file = getMockMultipartFile();

        BookGroupRegisterRequest nullRequest = new BookGroupRegisterRequest(null);

        assertThatThrownBy(() -> bookGroupManagementService.registerBookGroup(nullRequest, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 그룹명을 입력해주세요.");

        BookGroupRegisterRequest blankRequest = new BookGroupRegisterRequest("");

        assertThatThrownBy(() -> bookGroupManagementService.registerBookGroup(blankRequest, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 그룹명을 입력해주세요.");
    }

    @Test
    void whenBookGroupRegistered_thenVerifyIsRegistered(){
        MockMultipartFile file = getMockMultipartFile();
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("홍길동전");

        Long bookGroupId = bookGroupManagementService.registerBookGroup(request, file);

        BookGroup bookGroup = bookGroupManagementService.findBookGroupById(bookGroupId);

        assertThat(bookGroup.getId()).isEqualTo(bookGroupId);
        assertThat(bookGroup.getTitle()).isEqualTo("홍길동전");
        verify(imageUploadUtil).uploadImage(file);
    }

    private BookGroupManagementService getBookGroupManagementService() {
        imageUploadUtil = mock(ImageUploadUtil.class);
        BookGroupManagementService bookGroupManagementService = new BookGroupManagementService(bookGroupRepository, imageUploadUtil);
        return bookGroupManagementService;
    }

    private static MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "홍길동전 썸네일 이미지",
                "thumbnail.png",
                MediaType.IMAGE_PNG_VALUE,
                "thumbnail".getBytes()
        );
    }

}