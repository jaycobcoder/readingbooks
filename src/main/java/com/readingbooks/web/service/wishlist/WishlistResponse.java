package com.readingbooks.web.service.wishlist;

import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import lombok.Getter;

@Getter
public class WishlistResponse {
    private Long bookId;
    private Long wishlistId;
    private String title;
    private String isbn;
    private String savedImageName;
    private int ebookPrice;
    private int discountRate;
    private int salePrice;

    public WishlistResponse(Wishlist wishlist) {
        bookId = wishlist.getBook().getId();
        wishlistId = wishlist.getId();
        title = wishlist.getBook().getTitle();
        isbn = wishlist.getBook().getIsbn();
        savedImageName = wishlist.getBook().getSavedImageName();
        ebookPrice = wishlist.getBook().getEbookPrice();
        discountRate = wishlist.getBook().getDiscountRate();
        salePrice = wishlist.getBook().getSalePrice();
    }
}
