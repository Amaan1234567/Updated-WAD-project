package com.orders_service;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jspecify.annotations.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * OrderItem
 */

@Entity
@Table(name = "order_items", schema = "orders_svc", indexes = {
    @Index(name = "order_and_product_id_composite_index", columnList = "orderId,productId"),
    @Index(name = "order_id_composite_index", columnList = "orderId")  
})
@IdClass(OrderItemKey.class)
@JsonTypeName("order_item")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-22T23:15:29.240830294+05:30[Asia/Kolkata]", comments = "Generator version: 7.20.0")
public class OrderItem {

  @Id
  @Column(name = "order_id")
  private Long orderId;
  @Id
  @Column(name = "product_id")
  private Long productId;
  @Column(name = "quantity")
  private Integer quantity;
  @Column(name = "price_per_unit")
  private Double pricePerUnit;
  @Column(name = "subtotal")
  private Double subtotal;

  public OrderItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderItem(Long orderId, Long productId, Integer quantity, Double pricePerUnit, Double subtotal) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.pricePerUnit = pricePerUnit;
    this.subtotal = subtotal;
  }

  public OrderItem orderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
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

  public OrderItem productId(Long productId) {
    this.productId = productId;
    return this;
  }

  /**
   * Get productId
   * @return productId
   */
  @NotNull 
  @Schema(name = "product_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("product_id")
  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public OrderItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public OrderItem pricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
    return this;
  }

  /**
   * Get pricePerUnit
   * minimum: 0
   * @return pricePerUnit
   */
  @NotNull @DecimalMin(value = "0", inclusive = false) 
  @Schema(name = "price_per_unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price_per_unit")
  public Double getPricePerUnit() {
    return pricePerUnit;
  }

  public void setPricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }

  public OrderItem subtotal(Double subtotal) {
    this.subtotal = subtotal;
    return this;
  }

  /**
   * Get subtotal
   * minimum: 0
   * @return subtotal
   */
  @NotNull @DecimalMin(value = "0", inclusive = false) 
  @Schema(name = "subtotal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subtotal")
  public Double getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(Double subtotal) {
    this.subtotal = subtotal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItem orderItem = (OrderItem) o;
    return Objects.equals(this.orderId, orderItem.orderId) &&
        Objects.equals(this.productId, orderItem.productId) &&
        Objects.equals(this.quantity, orderItem.quantity) &&
        Objects.equals(this.pricePerUnit, orderItem.pricePerUnit) &&
        Objects.equals(this.subtotal, orderItem.subtotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, productId, quantity, pricePerUnit, subtotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderItem {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    pricePerUnit: ").append(toIndentedString(pricePerUnit)).append("\n");
    sb.append("    subtotal: ").append(toIndentedString(subtotal)).append("\n");
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

