package com.readingbooks.web.service.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookSearchResponse {
    private Long bookId;
    private String isbn;
    private String savedImageName;
    private String title;
    private String publisher;
    private String description;
    private int salePrice;
    private String author;
    private int authorCountExceptMainAuthor;
    private String translator;

    private String categoryGroupName;

    @QueryProjection
    public BookSearchResponse(Long bookId, String isbn, String savedImageName, String title, String publisher, String description, int ebookPrice, int discountRate, String author, Long authorCount, String translator, String categoryGroupName) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.savedImageName = savedImageName;
        this.title = title;
        this.publisher = publisher;
        this.description = cutDescription(description);
        this.salePrice = calculateSalePrice(ebookPrice, discountRate);
        this.author = author;
        if(authorCount == null){
            this.authorCountExceptMainAuthor = 0;
        }else{
            this.authorCountExceptMainAuthor = authorCount.intValue() - 1;
        }
        this.translator = translator;
        this.categoryGroupName = categoryGroupName;
    }

    private int calculateSalePrice(int ebookPrice, int discountRate) {
        int discountPrice = (int)(ebookPrice * discountRate * 0.01);
        return ebookPrice - discountPrice;
    }

    private String cutDescription(String description){
        if(description.length() > 150){
            return description.substring(0, 150) + "...";
        }
        return description.substring(0, description.length() - 1) + "...";
    }
}
