package com.readingbooks.web.repository.orders;

import com.readingbooks.web.domain.entity.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
