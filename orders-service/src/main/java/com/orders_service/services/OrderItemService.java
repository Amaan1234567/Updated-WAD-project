package com.orders_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orders_service.OrderItem;
import com.orders_service.OrderItemKey;
import com.orders_service.repositories.OrderItemsRepository;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    public void createOrderItem(OrderItem orderItem) {
        orderItemsRepository.save(orderItem);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemsRepository.searchByOrderId(orderId);
    }

    public void updateOrderItemByOrderAndProductId(OrderItem updatedOrderItem) {
        orderItemsRepository.save(updatedOrderItem);
    }

    public void deleteOrderItem(OrderItemKey orderItemKey) {
        orderItemsRepository.deleteById(orderItemKey);
    }
}
