package com.readingbooks.web.service.library;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.BookContent;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.exception.library.NotBoughtBookException;
import com.readingbooks.web.repository.bookcontent.BookContentRepository;
import com.readingbooks.web.repository.library.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookContentRepository bookContentRepository;

    /**
     * 구매한 도서 조회 메소드
     * @param member
     * @return List<DTO>
     */

    public List<LibraryResponse> findBooksInLibrary(Member member) {
        return libraryRepository.findAllByMemberId(member.getId()).stream()
                .map(l -> new LibraryResponse(l))
                .collect(Collectors.toList());
    }

    /**
     * 구매한 도서 내용 열람 메소드
     * @param member
     * @param bookId
     * @return
     */
    public BookContentResponse findBookContent(Member member, Long bookId) {
        /* --- 구매한 도서를 열람하는 지 확인 --- */
        boolean isBought = libraryRepository.existsByMemberIdAndBookId(member.getId(), bookId);

        if(isBought == false){
            throw new NotBoughtBookException("구매하지 않은 도서를 열람할 수 없습니다.");
        }

        BookContent bookContent = bookContentRepository.findBookContent(bookId);
        return new BookContentResponse(bookContent);
    }
}
