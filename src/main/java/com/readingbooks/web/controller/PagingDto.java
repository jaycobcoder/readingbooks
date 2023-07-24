package com.readingbooks.web.controller;

import lombok.Getter;

@Getter
public class PagingDto {
    // 페이지 그룹에 최대로 담길 수 있는 페이지 수량
    protected final int MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP = 5;

    // 페이지 그룹에 담길 수 있는 페이지 수량
    protected int pageGroupSize;

    // 몇 개의 페이지 그룹이 있는지 수량
    protected int totalPageGroups;

    // 페이지 그룹의 번호
    protected int pageGroupNumber;
    // 현재 페이지 그룹 번호에서 시작하는 페이지 번호
    protected int startPageNumberInThisPageGroup;
    // 현재 페이지 그룹 번호에서 끝나는 페이지 번호
    protected int lastPageNumberInThisPageGroup;

    // 이전 페이지 번호
    protected int prevPageNumber;
    // 다음 페이지 번호
    protected int nextPageNumber;


    protected int calculateTotalPageGroups(int totalSearchResultCount) {
        // 총 블록 개수는 = 총 검색 결과 / 블록 사이즈 -> 소수점은 무조건 반올림 처리
        return (int) Math.ceil(totalSearchResultCount * 1.0 / pageGroupSize);
    }

    protected int calculatePageGroupNumber(int number) {
        // double형이 아닌, int형이므로 소수점은 자동 제거
        return number / pageGroupSize;
    }

    protected int calculateStartPageNumber() {
        return (pageGroupNumber) * pageGroupSize + 1;
    }

    protected int calculateLastPageNumber(int totalPages, int startPageNumberInThisPageGroup) {
        int tempLastPageNumber = startPageNumberInThisPageGroup + pageGroupSize - 1;
        return tempLastPageNumber < totalPages ? tempLastPageNumber : totalPages;
    }
}
