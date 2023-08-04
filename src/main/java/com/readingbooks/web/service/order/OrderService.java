package com.readingbooks.web.service.order;

import com.readingbooks.web.domain.entity.book.Book;
import com.readingbooks.web.domain.entity.library.Library;
import com.readingbooks.web.domain.entity.member.Member;
import com.readingbooks.web.domain.entity.orders.OrderBooks;
import com.readingbooks.web.domain.entity.orders.Orders;
import com.readingbooks.web.domain.entity.wishlist.Wishlist;
import com.readingbooks.web.exception.book.BookPresentException;
import com.readingbooks.web.exception.orders.OrdersNotFoundException;
import com.readingbooks.web.repository.library.LibraryRepository;
import com.readingbooks.web.repository.orderbooks.OrderBooksRepository;
import com.readingbooks.web.repository.orders.OrdersRepository;
import com.readingbooks.web.repository.wishlist.WishlistRepository;
import com.readingbooks.web.service.book.BookService;
import com.readingbooks.web.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final BookService bookService;
    private final OrderBooksRepository orderBooksRepository;
    private final OrdersRepository ordersRepository;
    private final WishlistRepository wishlistRepository;
    private final LibraryRepository libraryRepository;
    private final MemberService memberService;

    /**
     * 결제 정보 조회 API
     * @param bookIdList
     * @param email
     * @return PayInformationResponse DTO
     */
    @Transactional(readOnly = true)
    public PayInformationResponse getPayInformation(List<Long> bookIdList, String email) {
        /* --- 도서 조회 --- */
        List<Book> books = bookService.findAllById(bookIdList);

        /* --- 회원 이름 조회 --- */
        Member member = memberService.findMember(email);
        String name = member.getName();

        /* --- 결제 정보 생성 --- */
        String orderNo = getOrderNo();
        String orderName = getOrderName(bookIdList);
        int orderAmount = getOrderAmount(books);
        int discountAmount = getDiscountAmount(books);
        int payAmount = orderAmount - discountAmount;

        return new PayInformationResponse(orderName, orderNo, email, orderAmount, discountAmount, payAmount, name);
    }

    private int getOrderAmount(List<Book> books) {
        return books.stream()
                .mapToInt(b -> b.getEbookPrice())
                .sum();
    }

    private int getDiscountAmount(List<Book> books) {
        int sum = 0;
        for (Book book : books) {
            sum += book.getEbookPrice() * book.getDiscountRate() * 0.01;
        }
        return sum;
    }

    public String getOrderName(List<Long> bookIdList) {
        Long bookId = bookIdList.get(0);
        Book book = bookService.findBook(bookId);
        String title = book.getTitle();

        int count = 0;
        int size = bookIdList.size();
        if(size > 1){
            count = size - 1;
        }

        if(count == 0){
            return title;
        }else{
            return title + " 외 " + count +"권";
        }
    }

    public String getOrderNo() {
        /* --- 주문 번호는 yyyy+MM+dd+랜덤 난수(100000~999999)로 구성 --- */
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomInt = random.nextInt(max - min + 1) + min;
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        return year + month + day + randomInt;
    }

    /**
     * 주문 메소드
     * @param member
     * @param books
     * @param orderRequest
     * @return 주문 ID
     */
    public Long order(Member member, List<Book> books, OrderRequest orderRequest) {
        /* --- 이미 구매한 도서인지 검증 --- */
        validateOrderingBooksWasBought(books, member.getId());

        /* --- 폼 검증 --- */
        validateForm(orderRequest.getOrderName(), orderRequest.getOrderNo(), orderRequest.getImpUid(), orderRequest.getChoosingOption(), orderRequest.getEmail(), orderRequest.getOrderAmount(), orderRequest.getDiscountAmount(), orderRequest.getPaymentAmount());

        /* --- 주문 생성 및 저장 --- */
        Orders orders = ordersRepository.save(Orders.createOrders(member, orderRequest));

        /* --- 주문한 도서들 생성 및 저장 --- */
        List<OrderBooks> orderBooksList = new ArrayList<>();
        for (Book book : books) {
            OrderBooks orderBooks = OrderBooks.createOrderBooks(book, orders);
            orderBooksList.add(orderBooks);
        }
        orderBooksRepository.saveAll(orderBooksList);

        /* --- 위시리스트 삭제 --- */
        deleteWishlist(member, books);

        /* --- 라이브러리에 주문한 도서 추가 --- */
        addBooksInLibrary(member, books);
        return orders.getId();
    }

    private void validateOrderingBooksWasBought(List<Book> books, Long memberId) {
        List<Long> bookIdList = new ArrayList<>();
        for (Book book : books) {
            bookIdList.add(book.getId());
        }
        boolean isExists = libraryRepository.existsByBookIdsAndMemberId(bookIdList, memberId);
        if(isExists == true){
            throw new BookPresentException("주문하고자 하는 도서 중 일부를 이미 구입하셨습니다. ");
        }
    }

    private void addBooksInLibrary(Member member, List<Book> books) {
        List<Library> libraryList = new ArrayList<>();
        for (Book book : books) {
            Library library = Library.createLibrary(member, book);
            libraryList.add(library);
        }
        libraryRepository.saveAll(libraryList);
    }

    private void deleteWishlist(Member member, List<Book> books) {
        List<Long> bookIdList = new ArrayList<>();
        for (Book book : books) {
            bookIdList.add(book.getId());
        }
        Long memberId = member.getId();
        List<Wishlist> wishlists = wishlistRepository.findByMemberIdAndBookIds(memberId, bookIdList);
        List<Long> wishlistIdList = wishlists.stream().map(w -> w.getId()).collect(Collectors.toList());
        wishlistRepository.deleteAllById(wishlistIdList);
    }

    private void validateForm(String orderName, String orderNo, String impUid, String choosingOption, String email, int orderAmount, int discountAmount, int paymentAmount) {
        if(orderName == null || orderName.trim().equals("")){
            throw new IllegalArgumentException("주문한 도서 이름을 입력해주세요.");
        }
        if(orderNo == null || orderNo.trim().equals("")){
            throw new IllegalArgumentException("주문 번호를 입력해주세요.");
        }
        if(impUid == null || impUid.trim().equals("")){
            throw new IllegalArgumentException("IMP UID를 입력해주세요.");
        }
        if(choosingOption == null || choosingOption.trim().equals("")){
            throw new IllegalArgumentException("주문 시 선택한 결제 옵션을 입력해주세요.");
        }
        if(email == null || email.trim().equals("")){
            throw new IllegalArgumentException("주문 당시 이메일을 입력해주세요.");
        }
        if(orderAmount == 0){
            throw new IllegalArgumentException("주문 금액이 0원일 수는 없습니다.");
        }
        if(paymentAmount == 0){
            throw new IllegalArgumentException("결제 금액이 0원일 수는 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Orders findOrders(Long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrdersNotFoundException("주문 아이디를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<OrderHistoryResponse> getHistory(Long memberId, Pageable pageable) {
        Page<Orders> responses = ordersRepository.findByMemberId(pageable, memberId);
        return responses.map(r -> new OrderHistoryResponse(r));
    }

    @Transactional(readOnly = true)
    public OrderFindResponse findOrderDetails(Long orderId) {
        Orders orders = findOrders(orderId);
        OrderFindResponse response = new OrderFindResponse(orders);
        return response;
    }

    @Transactional(readOnly = true)
    public List<OrderBooksResponse> findOrderBooksInOrders(Long ordersId) {
        return orderBooksRepository.findByOrdersId(ordersId).stream()
                .map(ob -> new OrderBooksResponse(ob.getBook()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SaleResponse findSalesOfTodayAndWeekAndMonth() {
        LocalDateTime today = LocalDateTime.now();

        /* --- 금일 수입 --- */
        LocalDateTime startOfToday = today.with(LocalTime.MIN);
        LocalDateTime endOfToday = today.with(LocalTime.MAX);
        List<Orders> todayOrders = ordersRepository.findByCreatedTimeBetween(startOfToday, endOfToday);
        int todaySales = sumOrdersPaymentAmount(todayOrders);

        /* --- 금주 수입 --- */
        LocalDateTime startOfWeek = today.toLocalDate().minusDays(today.getDayOfWeek().getValue() - 1).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7).minusNanos(1);
        List<Orders> weekOrders = ordersRepository.findByCreatedTimeBetween(startOfWeek, endOfWeek);
        int weekSales = sumOrdersPaymentAmount(weekOrders);

        /* --- 한달 수입 --- */
        LocalDateTime startOfMonth = today.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        List<Orders> monthOrders = ordersRepository.findByCreatedTimeBetween(startOfMonth, endOfMonth);
        int monthSales = sumOrdersPaymentAmount(monthOrders);

        return new SaleResponse(todaySales, weekSales, monthSales);
    }

    private int sumOrdersPaymentAmount(List<Orders> todayOrders) {
        return todayOrders.stream().mapToInt(Orders::getPaymentAmount).sum();
    }
}
