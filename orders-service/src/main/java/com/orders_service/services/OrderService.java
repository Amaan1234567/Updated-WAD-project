package com.orders_service.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.jdbc.env.spi.SQLStateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orders_service.Order;
import com.orders_service.Order.OrderStatusEnum;
import com.orders_service.OrderItem;
import com.orders_service.UserContextInterceptor;
import com.orders_service.repositories.OrderItemsRepository;
import com.orders_service.repositories.OrderRepository;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sets the Supabase auth UUID into the current transaction so RLS policies
     * fire.
     * Must be called inside a @Transactional method.
     */
    private void setRlsContext() {
        String supabaseAuthUuid = UserContextInterceptor.getUserUuid();
        String userRole = UserContextInterceptor.getUserRole(); // you'll need this
        if (supabaseAuthUuid != null) {
            String jwtClaims = String.format(
                    "{\"sub\": \"%s\", \"user_role\": \"%s\"}",
                    supabaseAuthUuid,
                    userRole != null ? userRole : "user");
            entityManager.createNativeQuery(
                    "SELECT set_config('request.jwt.claims', :claims, true)")
                    .setParameter("claims", jwtClaims)
                    .getSingleResult();
        }
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        setRlsContext();
        return orderRepository.findById(orderId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderByUserId(Long userId) {
        setRlsContext();
        return orderRepository.searchByUserId(userId);
    }

    @Transactional
    public Order createOrder(Order order) {
        setRlsContext();

        List<OrderItem> items = order.getOrderItems();
        order.setOrderItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);

        if (items != null) {
            for (OrderItem item : items) {
                item.setOrderId(order.getOrderId());
                orderItemsRepository.save(item);
            }
        }

        order.setOrderId(savedOrder.getOrderId());
        return order;
    }

    @Transactional
    public int updateOrder(Long orderId, @Nullable String deliveryAddress, @Nullable OrderStatusEnum orderStatus) {
        setRlsContext();

        String orderStatusString = null;
        if (orderStatus != null) {
            orderStatusString = orderStatus.toString();
        }
        int rowsUpdated = orderRepository.updateOrder(orderId, deliveryAddress, orderStatusString);

        return rowsUpdated;
    }

    @Transactional
    public ResponseEntity<String> deleteOrder(Long orderId) {
        setRlsContext();

        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok("Order Deleted");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatusCode.valueOf(404))
                        .body("Could not find record with id {" + orderId + "}"));
    }
}
