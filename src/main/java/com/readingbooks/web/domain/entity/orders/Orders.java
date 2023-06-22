package com.readingbooks.web.domain.entity.orders;

import com.readingbooks.web.domain.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    private String orderNo;
    private int orderAmount;
    private int discountAmount;
    private int usingPointAmount;
    private int paymentAmount;
    private String choosingOption;
    private int earnedPointAmount;
}
