package com.readingbooks.web.controller.wishlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/wishlist")
public class WishlistController {
    @GetMapping
    public String wishlist(){
        return "wishlist/wishlist";
    }
}
