package com.orders_service.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orders_service.Order;
import com.orders_service.Order.OrderStatusEnum;
import com.orders_service.OrderItem;
import com.orders_service.repositories.OrderItemsRepository;
import com.orders_service.repositories.OrderRepository;

import jakarta.annotation.Nullable;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.searchByUserId(userId);
    }

    @Transactional
    public Order createOrder(Order order) {
        List<OrderItem> items = order.getOrderItems();
        order.setOrderItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);

        if (items != null) {
            for (OrderItem item : items) {
                item.setOrderId(savedOrder.getOrderId());
                orderItemsRepository.save(item);
            }
        }

        order.setOrderId(savedOrder.getOrderId());
        return order;
    }

    @Transactional
    public void updateOrder(Long orderId, @Nullable String deliveryAddress, @Nullable OrderStatusEnum orderStatus) {
        String orderStatusString = null;
        if (orderStatus != null) {
            orderStatusString = orderStatus.toString();
        }
        orderRepository.updateOrder(orderId, deliveryAddress, orderStatusString);
    }

    public ResponseEntity<String> deleteOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok("Order Deleted");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatusCode.valueOf(404))
                        .body("Could not find record with id {" + orderId + "}"));
    }
}
