package com.orders_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import com.orders_service.Order.OrderStatusEnum;
import com.orders_service.services.OrderItemService;
import com.orders_service.services.OrderService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.annotation.Generated;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-22T23:12:50.165224919+05:30[Asia/Kolkata]", comments = "Generator version: 7.20.0")
@Controller
@RequestMapping("${openapi.orders.base-path:/v1}")
public class OrdersApiController implements OrdersApi {

    private final NativeWebRequest request;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    public ResponseEntity<Void> ordersOrderIdDelete(
            @NotNull @Parameter(name = "order_id", description = "Numeric id of order to delete", required = true, in = ParameterIn.PATH) @PathVariable("order_id") Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> ordersOrderIdGet(
            @NotNull @Parameter(name = "order_id", description = "Numeric id of order to get", required = true, in = ParameterIn.PATH) @PathVariable("order_id") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> ordersOrderIdItemsGet(
            @NotNull @Parameter(name = "order_id", description = "Numeric id of order to get", required = true, in = ParameterIn.PATH) @PathVariable("order_id") Long orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        if (orderItems == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(orderItems);
        }

    }

    public ResponseEntity<Void> ordersPost(
            @Parameter(name = "Order", description = "", required = true) @Valid @RequestBody Order order) {
        if (order.validate_subtotal_check() && order.validate_total_check() && order.validate_product_id_uniqueness()) {
            orderService.createOrder(order); // This now saves everything safely
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

    }

    public ResponseEntity<Void> ordersPut(
            @Parameter(name = "Order", description = "", required = true) @Valid @RequestBody OrderUpdateStatus orderUpdateStatus) {
        orderService.updateOrder(orderUpdateStatus.getOrderId(), null, orderUpdateStatus.getOrderStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> ordersUpdateAddress(
            @Parameter(name = "Order", description = "", required = true) @Valid @RequestBody OrderUpdateAddress orderAddressUpdate) {
        OrderStatusEnum orderStatus = orderService.getOrderById(orderAddressUpdate.getOrderId()).getOrderStatus();
        if (orderStatus == OrderStatusEnum.CONFIRMED || orderStatus == OrderStatusEnum.PENDING_PAYMENT) {
            orderService.updateOrder(orderAddressUpdate.getOrderId(), orderAddressUpdate.getOrderAddress(), null);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public ResponseEntity<?> ordersUserUserIdGet(
            @NotNull @Parameter(name = "user_id", description = "Numeric id of user whose orders to get", required = true, in = ParameterIn.PATH) @PathVariable("user_id") Long userId) {
        List<Order> orders = orderService.getOrderByUserId(userId);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    public OrdersApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
