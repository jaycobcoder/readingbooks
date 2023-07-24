package com.readingbooks.web.service.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayInformationResponse {
    private String orderName;
    private String orderNo;
    private String email;
    private int orderAmount;
    private int discountAmount;
    private int paymentAmount;
    private String name;
}
