package com.orders_service;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Order
 */

@Table(name = "orders", schema = "orders_svc", indexes = {
    @Index(name = "order_id_index", columnList = "orderId")
})
@Entity
@JsonTypeName("order")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-22T23:15:29.240830294+05:30[Asia/Kolkata]", comments = "Generator version: 7.20.0")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_id_generator")
  @SequenceGenerator(name = "order_id_generator", sequenceName = "id_gen", schema = "orders_svc", allocationSize = 10)
  private Long orderId;

  @Column(name = "customer_id")
  private Long customerId;

  @CreationTimestamp
  @Column(name = "order_date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime orderDate;

  @Column(name = "total_amount")
  private Double totalAmount;

  /**
   * Gets or Sets orderStatus
   */
  public enum OrderStatusEnum {
    PENDING_PAYMENT("PENDING_PAYMENT"),

    CONFIRMED("CONFIRMED"),

    SHIPPED("SHIPPED"),

    DELIVERED("DELIVERED"),

    CANCELLED("CANCELLED");

    private final String value;

    OrderStatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OrderStatusEnum fromValue(String value) {
      for (OrderStatusEnum b : OrderStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "order_status", columnDefinition = "orders_svc.order_status_type", nullable = true)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private OrderStatusEnum orderStatus = OrderStatusEnum.PENDING_PAYMENT;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @Column(name = "payment_details")
  @JdbcTypeCode(SqlTypes.JSON)
  private Object paymentDetails;

  @Column(name = "delivery_date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime deliveryDate;

  @OneToMany(fetch = FetchType.EAGER,mappedBy = "orderId", cascade = CascadeType.ALL,orphanRemoval = true)
  @Valid
  private List<@Valid OrderItem> orderItems = new ArrayList<>();

  public Order() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Order(Long orderId, Long customerId, Double totalAmount, OrderStatusEnum orderStatus, String deliveryAddress,
      Object paymentDetails, OffsetDateTime deliveryDate) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.totalAmount = totalAmount;
    this.orderStatus = orderStatus;
    this.deliveryAddress = deliveryAddress;
    this.paymentDetails = paymentDetails;
    this.deliveryDate = deliveryDate;
  }

  public Order orderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * 
   * @return orderId
   */
  @Schema(name = "order_id", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
  @JsonProperty("order_id")
  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Order customerId(Long customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * 
   * @return customerId
   */
  @NotNull
  @Schema(name = "customer_id", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customer_id")
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Order orderDate(@Nullable OffsetDateTime orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  /**
   * Get orderDate
   * 
   * @return orderDate
   */
  @Valid
  @Schema(name = "order_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_date")
  public OffsetDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(OffsetDateTime orderDate) {
    this.orderDate = orderDate;
  }

  /**
   * Get deliveryDate
   * 
   * @return deliveryDate
   */
  @Valid
  @Schema(name = "delivery_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delivery_date")
  public @Nullable OffsetDateTime getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(@Nullable OffsetDateTime deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public Order totalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  /**
   * Get totalAmount
   * minimum: 0
   * 
   * @return totalAmount
   */
  @NotNull
  @DecimalMin(value = "0", inclusive = false)
  @Schema(name = "total_amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_amount")
  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Order orderStatus(OrderStatusEnum orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  /**
   * Get orderStatus
   * 
   * @return orderStatus
   */
  @Schema(name = "order_status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("order_status")
  public OrderStatusEnum getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatusEnum orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Order deliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

  /**
   * Get deliveryAddress
   * 
   * @return deliveryAddress
   */
  @Schema(name = "delivery_address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delivery_address")
  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public Order paymentDetails(Object paymentDetails) {
    this.paymentDetails = paymentDetails;
    return this;
  }

  /**
   * json payment details returned from payment api
   * 
   * @return paymentDetails
   */
  @NotNull
  @Schema(name = "payment_details", description = "json payment details returned from payment api", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payment_details")
  public Object getPaymentDetails() {
    return paymentDetails;
  }

  public void setPaymentDetails(Object paymentDetails) {
    this.paymentDetails = paymentDetails;
  }

  public Order orderItems(List<@Valid OrderItem> orderItems) {
    this.orderItems = orderItems;
    return this;
  }

  public Order addOrderItemsItem(OrderItem orderItemsItem) {
    if (this.orderItems == null) {
      this.orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItemsItem);
    return this;
  }

  /**
   * Get orderItems
   * 
   * @return orderItems
   */
  @Valid
  @Schema(name = "order_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_items")
  public List<@Valid OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<@Valid OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public boolean validate_total_check() {
    float sum = 0.0f;
    for (OrderItem item : this.orderItems) {
      sum += item.getSubtotal();
    }
    if (sum != this.totalAmount) {
      return false;
    } else {
      return true;
    }
  }

  public boolean validate_product_id_uniqueness() {
    HashSet<Long> set = new HashSet<Long>();
    for (OrderItem item : this.orderItems) {
      if (set.contains(item.getProductId())) {
        return false;
      } else {
        set.add(item.getProductId());
      }
    }
    return true;
  }

  public boolean validate_subtotal_check() {
    for (OrderItem item : this.orderItems) {
      if (item.getQuantity() * item.getPricePerUnit() != item.getSubtotal()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(this.orderId, order.orderId) &&
        Objects.equals(this.customerId, order.customerId) &&
        Objects.equals(this.orderDate, order.orderDate) &&
        Objects.equals(this.totalAmount, order.totalAmount) &&
        Objects.equals(this.orderStatus, order.orderStatus) &&
        Objects.equals(this.deliveryAddress, order.deliveryAddress) &&
        Objects.equals(this.paymentDetails, order.paymentDetails) &&
        Objects.equals(this.orderItems, order.orderItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, customerId, orderDate, totalAmount, orderStatus, deliveryAddress, paymentDetails,
        orderItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Order {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
    sb.append("    totalAmount: ").append(toIndentedString(totalAmount)).append("\n");
    sb.append("    orderStatus: ").append(toIndentedString(orderStatus)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
    sb.append("    paymentDetails: ").append(toIndentedString(paymentDetails)).append("\n");
    sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
