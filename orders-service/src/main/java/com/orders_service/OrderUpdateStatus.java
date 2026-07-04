package com.orders_service;

import com.orders_service.Order.OrderStatusEnum;

public class OrderUpdateStatus {
    private Long orderId;
    private OrderStatusEnum orderStatus;

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatusEnum getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }
}
