package com.users_service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    USER("USER"),

    ADMIN("ADMIN"),

    CSR("CSR"),

    CUSTOM("CUSTOM");

    private final String value;

    UserType(String value) {
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
    public static UserType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (UserType b : UserType.values()) {
            if (b.value.equalsIgnoreCase(value) || b.name().equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
