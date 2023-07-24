package com.readingbooks.web.controller.order;

import com.readingbooks.web.controller.PagingDto;
import com.readingbooks.web.service.order.OrderHistoryResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingOrdersDto extends PagingDto {

    private List<Integer> pageGroupNumbers = new ArrayList<>();
    public PagingOrdersDto(Page<OrderHistoryResponse> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();

        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP;

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses.getNumber());

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;
    }
}