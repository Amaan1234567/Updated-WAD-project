package com.orders_service;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * OrderItem
 */

@JsonTypeName("order_item")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-22T23:15:29.240830294+05:30[Asia/Kolkata]", comments = "Generator version: 7.20.0")
public class OrderItem {

  private Integer orderId;

  private Integer productId;

  private Integer quantity;

  private Double pricePerUnit;

  private Double subtotal;

  public OrderItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderItem(Integer orderId, Integer productId, Integer quantity, Double pricePerUnit, Double subtotal) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.pricePerUnit = pricePerUnit;
    this.subtotal = subtotal;
  }

  public OrderItem orderId(Integer orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull 
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("order_id")
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public OrderItem productId(Integer productId) {
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
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
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

