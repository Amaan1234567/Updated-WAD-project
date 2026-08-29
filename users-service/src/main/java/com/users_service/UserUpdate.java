package com.users_service;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserUpdate
 */

@JsonTypeName("user_update")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-29T13:45:58.449049799+05:30[Asia/Kolkata]", comments = "Generator version: 7.21.0")
public class UserUpdate {

  private @Nullable UUID authId;

  private @Nullable String email;

  private @Nullable String password;

  private @Nullable String fullName;

  private @Nullable Integer phone;

  private @Nullable String address;

  private @Nullable String city;

  private @Nullable String state;

  private @Nullable String country;

  private @Nullable String pincode;

  /**
   * Gets or Sets userType
   */
  public enum UserTypeEnum {
    USER("user"),
    
    ADMIN("admin"),
    
    CSR("csr"),
    
    CUSTOM("custom");

    private final String value;

    UserTypeEnum(String value) {
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
    public static UserTypeEnum fromValue(String value) {
      for (UserTypeEnum b : UserTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable UserTypeEnum userType;

  public UserUpdate authId(@Nullable UUID authId) {
    this.authId = authId;
    return this;
  }

  /**
   * Get authId
   * @return authId
   */
  @Valid 
  @Schema(name = "auth_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auth_id")
  public @Nullable UUID getAuthId() {
    return authId;
  }

  @JsonProperty("auth_id")
  public void setAuthId(@Nullable UUID authId) {
    this.authId = authId;
  }

  public UserUpdate email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "general.kenobi123@starwars.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public UserUpdate password(@Nullable String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @Schema(name = "password", example = "hellothere", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public @Nullable String getPassword() {
    return password;
  }

  @JsonProperty("password")
  public void setPassword(@Nullable String password) {
    this.password = password;
  }

  public UserUpdate fullName(@Nullable String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
   */
  @Pattern(regexp = "/^\\p{L}+([\\s'-]\\p{L}+)*$/u") 
  @Schema(name = "full_name", example = "general kenobi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_name")
  public @Nullable String getFullName() {
    return fullName;
  }

  @JsonProperty("full_name")
  public void setFullName(@Nullable String fullName) {
    this.fullName = fullName;
  }

  public UserUpdate phone(@Nullable Integer phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   */
  
  @Schema(name = "phone", example = "9099472349", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone")
  public @Nullable Integer getPhone() {
    return phone;
  }

  @JsonProperty("phone")
  public void setPhone(@Nullable Integer phone) {
    this.phone = phone;
  }

  public UserUpdate address(@Nullable String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
   */
  @Pattern(regexp = "^[a-zA-Z0-9\\s.,'#-]{3,100}$") 
  @Schema(name = "address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public @Nullable String getAddress() {
    return address;
  }

  @JsonProperty("address")
  public void setAddress(@Nullable String address) {
    this.address = address;
  }

  public UserUpdate city(@Nullable String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  
  @Schema(name = "city", example = "bengaluru", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public @Nullable String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(@Nullable String city) {
    this.city = city;
  }

  public UserUpdate state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", example = "karnataka", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(@Nullable String state) {
    this.state = state;
  }

  public UserUpdate country(@Nullable String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
   */
  
  @Schema(name = "country", example = "india", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("country")
  public @Nullable String getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(@Nullable String country) {
    this.country = country;
  }

  public UserUpdate pincode(@Nullable String pincode) {
    this.pincode = pincode;
    return this;
  }

  /**
   * Get pincode
   * @return pincode
   */
  
  @Schema(name = "pincode", example = "560071", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pincode")
  public @Nullable String getPincode() {
    return pincode;
  }

  @JsonProperty("pincode")
  public void setPincode(@Nullable String pincode) {
    this.pincode = pincode;
  }

  public UserUpdate userType(@Nullable UserTypeEnum userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
   */
  
  @Schema(name = "user_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user_type")
  public @Nullable UserTypeEnum getUserType() {
    return userType;
  }

  @JsonProperty("user_type")
  public void setUserType(@Nullable UserTypeEnum userType) {
    this.userType = userType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserUpdate userUpdate = (UserUpdate) o;
    return Objects.equals(this.authId, userUpdate.authId) &&
        Objects.equals(this.email, userUpdate.email) &&
        Objects.equals(this.password, userUpdate.password) &&
        Objects.equals(this.fullName, userUpdate.fullName) &&
        Objects.equals(this.phone, userUpdate.phone) &&
        Objects.equals(this.address, userUpdate.address) &&
        Objects.equals(this.city, userUpdate.city) &&
        Objects.equals(this.state, userUpdate.state) &&
        Objects.equals(this.country, userUpdate.country) &&
        Objects.equals(this.pincode, userUpdate.pincode) &&
        Objects.equals(this.userType, userUpdate.userType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authId, email, password, fullName, phone, address, city, state, country, pincode, userType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdate {\n");
    sb.append("    authId: ").append(toIndentedString(authId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append("*").append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    pincode: ").append(toIndentedString(pincode)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

