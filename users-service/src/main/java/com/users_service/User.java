package com.users_service;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "users", schema = "users_svc", indexes = {
        @Index(name = "user_id_index", columnList = "userId")
})
@Entity
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "id_gen", schema = "users_svc", allocationSize = 10)
    private Long userId;

    @Column(name = "auth_id", nullable = false)
    @UUID
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;

    @Nullable
    @Column(name = "full_name")
    private String fullName;

    @Nullable
    private Integer phone;
    
    @Nullable
    private String address;
    
    @Nullable
    private String city;
    
    @Nullable
    private String state;
    
    @Nullable
    private String country;
    
    @Nullable
    private String pincode;

    @Column(name = "user_type", nullable = false, columnDefinition = "users_svc.user_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedTimestamp;

    User(String password, String email, String fullName, Integer phone, String address, String city, String state,
            String country, String pincode, UserType userType, Timestamp createdTimestamp, Timestamp updatedTimestamp) {
        this.password = password;
        this.email = email;
        this.fullName = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.userType = userType;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public User() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the authId
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * @param authId the authId to set
     */
    public void setAuthId(String authId) {
        this.authId = authId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the phone
     */
    public Integer getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the pincode
     */
    public String getPincode() {
        return pincode;
    }

    /**
     * @param pincode the pincode to set
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * @return the userType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * @return the createdTimestamp
     */
    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * @param createdTimestamp the createdTimestamp to set
     */
    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * @return the updatedTimestamp
     */
    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    /**
     * @param updatedTimestamp the updatedTimestamp to set
     */
    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public static User newUserApiToUser(NewUserApi newUserApi) {
        User user = new User();
        user.email = newUserApi.getEmail();
        user.fullName = newUserApi.getFullName();
        user.address = newUserApi.getAddress();
        user.phone = newUserApi.getPhone();
        user.city = newUserApi.getCity();
        user.state = newUserApi.getState();
        user.country = newUserApi.getCountry();
        user.pincode = newUserApi.getPincode();
        user.userType = UserType.fromValue(newUserApi.getUserType().toString());
        return user;
    }

    public static User newUserToUser(NewUser newUser) {
        User user = new User();
        user.email = newUser.getEmail();
        user.fullName = newUser.getFullName();
        user.phone = newUser.getPhone();
        user.address = newUser.getAddress();
        user.city = newUser.getCity();
        user.state = newUser.getState();
        user.country = newUser.getCountry();
        user.pincode = newUser.getPincode();
        user.userType = UserType.USER;
        return user;
    }

    public static User userUpdateToUser(UserUpdate userUpdate) {
        User user = new User();
        user.userId = userUpdate.getUserId();
        user.email = userUpdate.getEmail();
        user.fullName = userUpdate.getFullName();
        user.address = userUpdate.getAddress();
        user.phone = userUpdate.getPhone();
        user.city = userUpdate.getCity();
        user.state = userUpdate.getState();
        user.country = userUpdate.getCountry();
        user.pincode = userUpdate.getPincode();
        user.userType = UserType.fromValue(userUpdate.getUserType().toString());
        return user;
    }

}
