package com.example.carrentalservice.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AppUserPermission {
    CAR_READ("car:raed"),
    CAR_WRITE("car:write"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write"),
    MANAGER_READ("manager:read"),
    MANAGER_WRITE("manager:write");

    private final String Permission;
}