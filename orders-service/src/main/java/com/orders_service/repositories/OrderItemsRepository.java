package com.orders_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.orders_service.OrderItem;
import com.orders_service.OrderItemKey;

public interface OrderItemsRepository extends JpaRepository<OrderItem, OrderItemKey>{
     @Query(value = "SELECT * FROM orders_svc.order_items o WHERE " + 
        "(o.order_id = :orderId)",nativeQuery = true
    )
    List<OrderItem> searchByOrderId(@Param("orderId") Long orderId);
}
