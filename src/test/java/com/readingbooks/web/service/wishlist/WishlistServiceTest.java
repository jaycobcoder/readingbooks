package com.readingbooks.web.service.wishlist;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.wishlist.WishlistException;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.member.MemberRepository;
import com.readingbooks.web.repository.wishlist.WishlistRepository;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class WishlistServiceTest {
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void whenAddingSameBook_thenThrowException(){
        Book book = new Book();
        Member member = new Member();
        Long bookId = bookRepository.save(book).getId();
        Book savedBook = bookRepository.findById(bookId).get();
        Long memberId = memberRepository.save(member).getId();
        Member savedMember = memberRepository.findById(memberId).get();

        wishlistService.addBook(savedMember, savedBook);

        assertThatThrownBy(() -> wishlistService.addBook(savedMember, savedBook))
                .isInstanceOf(BookPresentException.class)
                .hasMessageContaining("위시리스트에 이미 해당 도서가 존재합니다.");
    }

    @Test
    void whenBookAdded_thenVerifyFields(){
        Book book = new Book();
        Member member = new Member();
        Long bookId = bookRepository.save(book).getId();
        Book savedBook = bookRepository.findById(bookId).get();
        Long memberId = memberRepository.save(member).getId();
        Member savedMember = memberRepository.findById(memberId).get();

        Long wishlistId = wishlistService.addBook(savedMember, savedBook);

        Wishlist wishlist = wishlistService.findWishlist(wishlistId);

        assertThat(wishlist.getMember().getId()).isEqualTo(memberId);
        assertThat(wishlist.getBook().getId()).isEqualTo(bookId);
    }

    @Test
    void whenDeletingBookNotMatchMemberId_throwException(){
        Book book = new Book();
        Member member = new Member();
        Long bookId = bookRepository.save(book).getId();
        Book savedBook = bookRepository.findById(bookId).get();
        Long memberId = memberRepository.save(member).getId();
        Member savedMember = memberRepository.findById(memberId).get();

        Long wishlistId = wishlistService.addBook(savedMember, savedBook);

        List<Long> wishlistIdList = new ArrayList<>();
        wishlistIdList.add(wishlistId);

        assertThatThrownBy(() -> wishlistService.delete(wishlistIdList, memberId + 1L))
                .isInstanceOf(WishlistException.class)
                .hasMessageContaining("본인이 추가하지 않은 도서를 장바구니에서 제거할 수 없습니다.");
    }

    @Test
    void whenBookDeleted_thenVerifyIsDeleted(){
        Book book = new Book();
        Member member = new Member();
        Long bookId = bookRepository.save(book).getId();
        Book savedBook = bookRepository.findById(bookId).get();
        Long memberId = memberRepository.save(member).getId();
        Member savedMember = memberRepository.findById(memberId).get();

        Long wishlistId = wishlistService.addBook(savedMember, savedBook);

        List<Long> wishlistIdList = new ArrayList<>();
        wishlistIdList.add(wishlistId);

        boolean isDeleted = wishlistService.delete(wishlistIdList, memberId);
        assertThat(isDeleted).isTrue();
    }
}