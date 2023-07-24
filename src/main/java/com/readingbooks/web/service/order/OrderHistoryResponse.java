package com.readingbooks.web.service.order;

import com.readingbooks.web.domain.entity.orders.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderHistoryResponse {
    private Long orderId;
    private String purchasedDate;
    private String orderName;
    private int paymentAmount;
    private String choosingOption;

    public OrderHistoryResponse(Orders orders) {
        this.orderId = orders.getId();
        LocalDateTime currentDate = orders.getCreatedTime();
        this.purchasedDate = createPurchasedDate(currentDate);
        this.orderName = orders.getOrderName();
        this.paymentAmount = orders.getPaymentAmount();
        this.choosingOption = orders.getChoosingOption();
    }

    private String createPurchasedDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        int hour = currentDate.toLocalTime().getHour();
        int minute = currentDate.getMinute();
        return year + "." + month + "." + day + ". " + hour + ":" + minute;
    }
}
