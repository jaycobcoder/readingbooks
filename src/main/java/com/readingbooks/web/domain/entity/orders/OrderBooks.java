package com.readingbooks.web.domain.entity.orders;

import com.readingbooks.web.domain.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class OrderBooks extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_books_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;
}
