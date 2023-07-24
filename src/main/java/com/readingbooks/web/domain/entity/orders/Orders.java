package com.readingbooks.web.domain.entity.orders;

import com.readingbooks.web.domain.entity.BaseEntity;
import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.service.order.OrderRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String orderName;
    private String orderNo;
    private String impUid;
    private String choosingOption;
    private String email;
    private int orderAmount;
    private int discountAmount;
    private int paymentAmount;

    @OneToMany(mappedBy = "orders")
    private List<OrderBooks> orderBooksList = new ArrayList<>();

    public static Orders createOrders(Member member, OrderRequest orderRequest) {
        Orders orders = new Orders();
        orders.member = member;
        orders.orderName = orderRequest.getOrderName();
        orders.orderNo = orderRequest.getOrderNo();
        orders.impUid = orderRequest.getImpUid();
        orders.choosingOption = orderRequest.getChoosingOption();
        orders.email = orderRequest.getEmail();
        orders.orderAmount = orderRequest.getOrderAmount();
        orders.discountAmount = orderRequest.getDiscountAmount();
        orders.paymentAmount = orderRequest.getPaymentAmount();
        return orders;
    }
}