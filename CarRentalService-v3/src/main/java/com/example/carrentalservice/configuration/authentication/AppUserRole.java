package com.example.carrentalservice.configuration.authentication;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.carrentalservice.configuration.authentication.AppUserPermission.*;

@AllArgsConstructor
@Getter
public enum AppUserRole {
    CUSTOMER(Sets.newHashSet(CAR_READ, ORDER_READ, ORDER_WRITE)),
    MANAGER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(CAR_READ, CAR_WRITE, ORDER_READ, ORDER_WRITE, USER_READ, USER_WRITE));

    private final Set<AppUserPermission> permissions;

    // for Permission-Based Authentication
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
