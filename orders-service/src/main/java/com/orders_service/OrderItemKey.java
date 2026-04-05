package com.orders_service;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemKey implements Serializable {

    private Long orderId;

    private Long productId;

    // Standard no-args constructor required by JPA
    public OrderItemKey() {
    }

    public OrderItemKey(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // Mandatory for Composite Keys
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderItemKey that = (OrderItemKey) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
