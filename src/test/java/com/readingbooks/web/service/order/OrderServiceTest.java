package com.readingbooks.web.service.order;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.orders.OrderBooks;
import com.readingbooks.web.domain.entity.orders.Orders;
import com.readingbooks.web.domain.enums.Gender;
import com.readingbooks.web.repository.book.BookRepository;
import com.readingbooks.web.repository.member.MemberRepository;
import com.readingbooks.web.repository.orderbooks.OrderBooksRepository;
import com.readingbooks.web.service.member.RegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderBooksRepository orderBooksRepository;

    @Test
    void orderNo(){
        String orderNo = orderService.getOrderNo();
        System.out.println("orderNo = " + orderNo);
        assertThat(orderNo.length()).isEqualTo(14);
    }

    @Test
    void whenOrderedBooks_thenVerifyFields(){
        Member member = memberRepository.save(new Member());
        Long memberId = member.getId();

        List<Book> books = new ArrayList<>();
        int orderCount = 5;
        for(int i = 0; i < orderCount; i++){
            Book book = bookRepository.save(new Book());
            books.add(book);
        }
        List<Long> bookIdList = books.stream().map(b -> b.getId()).collect(Collectors.toList());
        OrderRequest orderRequest = new OrderRequest(
                "test 외 2권", "2023020211215", "test", "card", "test@test", 10000, 5000, 5000,
                bookIdList
        );
        Long orderId = orderService.order(member, books, orderRequest);
        Orders orders = orderService.findOrders(orderId);

        assertThat(orders.getOrderName()).isEqualTo("test 외 2권");
        assertThat(orders.getOrderNo()).isEqualTo("2023020211215");
        assertThat(orders.getImpUid()).isEqualTo("test");
        assertThat(orders.getChoosingOption()).isEqualTo("card");
        assertThat(orders.getEmail()).isEqualTo("test@test");
        assertThat(orders.getOrderAmount()).isEqualTo(10000);
        assertThat(orders.getDiscountAmount()).isEqualTo(5000);
        assertThat(orders.getPaymentAmount()).isEqualTo(5000);

        assertThat(orders.getMember().getId()).isEqualTo(memberId);

        List<OrderBooks> orderBooksList = orderBooksRepository.findByOrdersId(orderId);
        assertThat(orderBooksList.size()).isEqualTo(orderCount);
    }
}