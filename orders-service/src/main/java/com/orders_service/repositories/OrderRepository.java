package com.orders_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.orders_service.Order;
import com.orders_service.Order.OrderStatusEnum;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders_svc.orders o WHERE " + 
        "(o.customer_id = :userId)",nativeQuery = true
    )
    List<Order> searchByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE orders_svc.orders " +
            "SET delivery_address = COALESCE(:deliveryAddress, delivery_address), " +
            "    order_status = COALESCE(CAST(:orderStatus AS orders_svc.order_status_type), order_status) " +
            "WHERE order_id = :orderId", nativeQuery = true)
    void updateOrder(@Param("orderId") Long orderId,
            @Param("deliveryAddress") String deliveryAddress,
            @Param("orderStatus") String orderStatus);
}
