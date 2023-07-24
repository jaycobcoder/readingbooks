package com.readingbooks.web.service.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequest {
    private String orderName;
    private String orderNo;
    private String impUid;
    private String choosingOption;
    private String email;
    private Integer orderAmount;
    private Integer discountAmount;
    private Integer paymentAmount;
    List<Long> bookIdList;
}
