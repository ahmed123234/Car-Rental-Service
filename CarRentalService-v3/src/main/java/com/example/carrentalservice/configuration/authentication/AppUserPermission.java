package com.example.carrentalservice.configuration.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AppUserPermission {
    CAR_READ("car:raed"),
    CAR_WRITE("car:write"),
    USER_READ("appUser:read"),
    USER_WRITE("appUser:write"),
    ORDER_READ("rentOrder:read"),
    ORDER_WRITE("rentOrder:write");

    private final String Permission;
}