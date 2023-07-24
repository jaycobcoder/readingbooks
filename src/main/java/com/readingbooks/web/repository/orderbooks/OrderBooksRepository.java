package com.readingbooks.web.repository.orderbooks;

import com.readingbooks.web.domain.entity.orders.OrderBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderBooksRepository extends JpaRepository<OrderBooks, Long> {

    List<OrderBooks> findByOrdersId(Long ordersId);

}
