package com.readingbooks.web.repository.orderbooks;

import com.readingbooks.web.domain.entity.orders.OrderBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderBooksRepository extends JpaRepository<OrderBooks, Long> {
    @Query("select ob " +
            "from OrderBooks ob " +
            "join ob.orders " +
            "join ob.book " +
            "where ob.orders.id = :ordersId")
    List<OrderBooks> findByOrdersId(@Param("ordersId") Long ordersId);

}
