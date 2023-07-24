package com.readingbooks.web.domain.entity.orders;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.Book;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderBooks extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_books_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public static OrderBooks createOrderBooks(Book book, Orders orders) {
        OrderBooks orderBooks = new OrderBooks();
        orderBooks.book = book;
        orderBooks.orders = orders;
        return orderBooks;
    }
}
