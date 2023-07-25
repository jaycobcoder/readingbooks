package com.readingbooks.web.service.review;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.review.Review;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void whenReviewed_thenVerifyFields(){
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Book book = new Book();
        Book savedBook = bookRepository.save(book);

        Long reviewId = reviewService.review(savedMember, savedBook, "test", 5);
        Review review = reviewService.findReview(reviewId);

        assertThat(review.getId()).isEqualTo(reviewId);
        assertThat(review.getMember().getId()).isEqualTo(savedMember.getId());
        assertThat(review.getBook().getId()).isEqualTo(savedBook.getId());
        assertThat(review.getContent()).isEqualTo("test");
        assertThat(review.getStarRating()).isEqualTo(5);
        assertThat(review.isPurchased()).isFalse();
        assertThat(review.isHidden()).isFalse();
        assertThat(review.getLikesCount()).isEqualTo(0);
        assertThat(savedBook.getReviewCount()).isEqualTo(1);
    }
}