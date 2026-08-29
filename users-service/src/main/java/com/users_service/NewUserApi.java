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
 * NewUserApi
 */

@JsonTypeName("new_user_api")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-29T13:45:58.449049799+05:30[Asia/Kolkata]", comments = "Generator version: 7.21.0")
public class NewUserApi {

  private UUID authId;

  private String email;

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

  private UserTypeEnum userType;

  public NewUserApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NewUserApi(UUID authId, String email, UserTypeEnum userType) {
    this.authId = authId;
    this.email = email;
    this.userType = userType;
  }

  public NewUserApi authId(UUID authId) {
    this.authId = authId;
    return this;
  }

  /**
   * Get authId
   * @return authId
   */
  @NotNull @Valid 
  @Schema(name = "auth_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("auth_id")
  public UUID getAuthId() {
    return authId;
  }

  @JsonProperty("auth_id")
  public void setAuthId(UUID authId) {
    this.authId = authId;
  }

  public NewUserApi email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "general.kenobi123@starwars.com", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }

  public NewUserApi password(@Nullable String password) {
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

  public NewUserApi fullName(@Nullable String fullName) {
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

  public NewUserApi phone(@Nullable Integer phone) {
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

  public NewUserApi address(@Nullable String address) {
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

  public NewUserApi city(@Nullable String city) {
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

  public NewUserApi state(@Nullable String state) {
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

  public NewUserApi country(@Nullable String country) {
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

  public NewUserApi pincode(@Nullable String pincode) {
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

  public NewUserApi userType(UserTypeEnum userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
   */
  @NotNull 
  @Schema(name = "user_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("user_type")
  public UserTypeEnum getUserType() {
    return userType;
  }

  @JsonProperty("user_type")
  public void setUserType(UserTypeEnum userType) {
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
    NewUserApi newUserApi = (NewUserApi) o;
    return Objects.equals(this.authId, newUserApi.authId) &&
        Objects.equals(this.email, newUserApi.email) &&
        Objects.equals(this.password, newUserApi.password) &&
        Objects.equals(this.fullName, newUserApi.fullName) &&
        Objects.equals(this.phone, newUserApi.phone) &&
        Objects.equals(this.address, newUserApi.address) &&
        Objects.equals(this.city, newUserApi.city) &&
        Objects.equals(this.state, newUserApi.state) &&
        Objects.equals(this.country, newUserApi.country) &&
        Objects.equals(this.pincode, newUserApi.pincode) &&
        Objects.equals(this.userType, newUserApi.userType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authId, email, password, fullName, phone, address, city, state, country, pincode, userType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewUserApi {\n");
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

