package com.readingbooks.web.controller.manage;

import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.order.OrderService;
import com.readingbooks.web.service.order.SaleResponse;
import com.readingbooks.web.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManagementHomeController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @GetMapping("/home")
    public String home(Model model){

        SaleResponse sales = orderService.findSalesOfTodayAndWeekAndMonth();
        model.addAttribute("sales", sales);

        int todayRegisteredMember = memberService.findTodayRegisterMemberCount();
        model.addAttribute("registeredMemberCount", todayRegisteredMember);

        int todayReviewCount = reviewService.findTodayReviewCount();
        model.addAttribute("todayReviewCount", todayReviewCount);
        return "manage/home";
    }
}
