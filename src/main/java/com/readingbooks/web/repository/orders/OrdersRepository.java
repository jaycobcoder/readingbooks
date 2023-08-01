package com.readingbooks.web.repository.orders;

import com.readingbooks.web.domain.entity.orders.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findByMemberId(Pageable pageable, Long memberId);

    List<Orders> findByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(
            "update Orders o " +
                    "set o.member = null " +
                    "where o.id in (:orderIds)"
    )
    void bulkMemberIdNull(@Param("orderIds") List<Long> orderIds);
}
