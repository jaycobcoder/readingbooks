package com.readingbooks.web.controller.search;

import com.readingbooks.web.controller.PagingDto;
import com.readingbooks.web.service.search.BookSearchResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingSearchDto extends PagingDto {
    // 최초 페이지 번호
    private int firstPageNumber;
    // 마지막 페이지 번호
    private int lastPageNumber;

    private List<Integer> pageGroupNumbers = new ArrayList<>();

    public PagingSearchDto(Page<BookSearchResponse> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();

        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP;

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses.getNumber());

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;

        firstPageNumber = 0;
        // 페이지는 0부터 시작하므로 -을 해줘야함.
        lastPageNumber = responses.getTotalPages() - 1;
    }
}
