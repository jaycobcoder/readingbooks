package com.readingbooks.web.controller.search;

import com.readingbooks.web.service.search.BookSearchResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingDto {
    // 페이지 그룹에 최대로 담길 수 있는 페이지 수량
    private final int MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP = 5;
    
    // 페이지 그룹에 담길 수 있는 페이지 수량
    private int pageGroupSize;
    
    // 몇 개의 페이지 그룹이 있는지 수량
    private int totalPageGroups;
    
    // 페이지 그룹의 번호
    private int pageGroupNumber;
    // 현재 페이지 그룹 번호에서 시작하는 페이지 번호
    private int startPageNumberInThisPageGroup;
    // 현재 페이지 그룹 번호에서 끝나는 페이지 번호
    private int lastPageNumberInThisPageGroup;

    // 이전 페이지 번호
    private int prevPageNumber;
    // 다음 페이지 번호
    private int nextPageNumber;
    
    // 최초 페이지 번호
    private int firstPageNumber;
    // 마지막 페이지 번호
    private int lastPageNumber;

    private List<Integer> pageGroupNumbers = new ArrayList<>();

    private boolean isFirstGroup;
    private boolean isLastGroup;


    public PagingDto(Page<BookSearchResponse> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();

        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP;

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses);

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;

        firstPageNumber = 0;
        // 페이지는 0부터 시작하므로 -을 해줘야함.
        lastPageNumber = responses.getTotalPages() - 1;
    }

    private int calculateTotalPageGroups(int totalSearchResultCount) {
        // 총 블록 개수는 = 총 검색 결과 / 블록 사이즈 -> 소수점은 무조건 반올림 처리
        return (int) Math.ceil(totalSearchResultCount * 1.0 / pageGroupSize);
    }

    private int calculatePageGroupNumber(Page<BookSearchResponse> responses) {
        // double형이 아닌, int형이므로 소수점은 자동 제거
        return responses.getNumber() / pageGroupSize;
    }

    private int calculateStartPageNumber() {
        return (pageGroupNumber) * pageGroupSize + 1;
    }

    private int calculateLastPageNumber(int totalPages, int startPageNumberInThisPageGroup) {
        int tempLastPageNumber = startPageNumberInThisPageGroup + pageGroupSize - 1;
        return tempLastPageNumber < totalPages ? tempLastPageNumber : totalPages;
    }
}
