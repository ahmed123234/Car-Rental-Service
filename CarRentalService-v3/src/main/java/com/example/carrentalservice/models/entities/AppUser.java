package com.example.carrentalservice.models.entities;

import com.example.carrentalservice.configuration.authentication.AppUserRole;
import com.example.carrentalservice.configuration.authentication.AuthenticationProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
@Configuration
@Getter // initialize getter methods using lombok dependency.
@Setter // initialize setter methods using lombok dependency.
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;
   @Column
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppUserRole appUserRole;

    @Enumerated(EnumType.STRING)
    private AuthenticationProvider authenticationProvider=AuthenticationProvider.LOCAL;
    @Column(nullable = false)
    private Boolean locked = false;
    @Column(nullable = false)
    private Boolean enabled = false;

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String username,
                   String password,
                   AppUserRole appUserRole) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}