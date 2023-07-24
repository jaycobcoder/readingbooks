package com.readingbooks.web.service.wishlist;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import com.readingbooks.web.exception.base.PresentException;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.wishlist.WishlistException;
import com.readingbooks.web.exception.wishlist.WishlistNotFoundException;
import com.readingbooks.web.repository.library.LibraryRepository;
import com.readingbooks.web.repository.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final LibraryRepository libraryRepository;

    /**
     * 위시리스트에 도서를 담는 메소드
     * @param member
     * @param book
     * @return
     */
    public Long addBook(Member member, Book book) {
        Long memberId = member.getId();
        Long bookId = book.getId();

        /* --- 도서가 이미 위시리스트에 담겨 있는지 확인 --- */
        validateIsBookExisted(bookId, memberId, new BookPresentException("위시리스트에 이미 해당 도서가 존재합니다."));

        /* --- 위시리스트에 담으려고 하는 도서를 구매했는지 확인 --- */
        validateAddingBookAlreadyBuyed(book);


        Wishlist wishlist = Wishlist.createWishlist(book, member);

        return wishlistRepository.save(wishlist).getId();
    }

    private void validateAddingBookAlreadyBuyed(Book book) {
        Long bookId = book.getId();
        boolean isExistsBook = libraryRepository.existsByBookId(bookId);
        if(isExistsBook == true){
            throw new BookPresentException("이미 구매한 도서입니다.");
        }
    }

    /**
     * 위시리스트에서 도서를 제거하는 메소드
     * @param wishlistIdList
     * @param memberId
     * @return isDeleted
     */
    public boolean delete(List<Long> wishlistIdList, Long memberId) {
        List<Wishlist> wishlists = findWishlist(wishlistIdList);

        if(wishlists.size() == 0){
            throw new WishlistException("삭제하고자 하는 도서가 존재하지 않습니다.");
        }

        for (Wishlist wishlist : wishlists) {
            Long findMemberId = wishlist.getMember().getId();
            if(findMemberId != memberId){
                throw new WishlistException("본인이 추가하지 않은 도서를 장바구니에서 제거할 수 없습니다.");
            }
        }

        wishlistRepository.deleteAllById(wishlistIdList);
        return true;
    }

    private List<Wishlist> findWishlist(List<Long> wishlistIdList) {
        return wishlistRepository.findAllById(wishlistIdList);
    }

    private void validateIsBookExisted(Long bookId, Long memberId, PresentException e) {
        boolean isExists = wishlistRepository.existsByBookIdAndMemberId(bookId, memberId);
        if(isExists == true){
            throw e;
        }
    }

    public Wishlist findWishlist(Long wishlistId){
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException("위시리스트 아이디를 다시 확인해주세요."));
    }

    public List<WishlistResponse> findBookResponses(Long memberId) {
        return wishlistRepository.findByMemberId(memberId).stream()
                .map(w -> new WishlistResponse(w))
                .collect(Collectors.toList());
    }
}
