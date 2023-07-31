package com.readingbooks.web.controller.order;

import com.readingbooks.web.service.member.MemberService;
import com.readingbooks.web.service.order.OrderBooksResponse;
import com.readingbooks.web.service.order.OrderFindResponse;
import com.readingbooks.web.service.order.OrderHistoryResponse;
import com.readingbooks.web.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderViewController {
    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/history")
    public String history(@PageableDefault(size = 15) Pageable pageable, Principal principal,
                          Model model){
        String email = principal.getName();
        Long memberId = memberService.findMember(email).getId();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "createdTime");

        Page<OrderHistoryResponse> responses = orderService.getHistory(memberId, pageRequest);
        PagingOrdersDto paging = new PagingOrdersDto(responses);
        model.addAttribute("responses", responses);
        model.addAttribute("paging", paging);
        model.addAttribute("active", "orders");
        return "user/orders/history";
    }

    @GetMapping("/{ordersId}")
    public String home(@PathVariable Long ordersId, Model model){
        OrderFindResponse response = orderService.getOrderDetail(ordersId);
        List<OrderBooksResponse> books = orderService.getOrderBooks(ordersId);

        model.addAttribute("response", response);
        model.addAttribute("books", books);
        return "user/orders/detail";
    }
}
