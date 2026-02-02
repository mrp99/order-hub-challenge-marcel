package com.orderhub.api.enums;

public enum OrderStatus {

    CREATED("CREATED"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED");

    private final String value;
    OrderStatus(String value) { this.value = value; }
    public String getValue() { return value; }
}
