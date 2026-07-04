package com.orders_service;

public class OrderUpdateAddress {
    private Long orderId;
    private String orderAddress;

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderAddress() {
        return this.orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
}
