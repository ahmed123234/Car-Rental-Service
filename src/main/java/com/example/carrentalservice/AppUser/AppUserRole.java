package com.example.carrentalservice.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import com.google.common.collect.Sets;

import static com.example.carrentalservice.AppUser.AppUserPermission.*;

@AllArgsConstructor
@Getter
public enum AppUserRole {
    CUSTOMER(Sets.newHashSet()),
    MANAGER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(CAR_READ, CAR_WRITE, CUSTOMER_READ,
            CUSTOMER_WRITE, MANAGER_READ, MANAGER_WRITE));

    private final Set<AppUserPermission> permissions;
}
