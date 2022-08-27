package com.example.carrentalservice.models.entities;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@Getter // initialize getter methods using lombok dependency.
@Setter // initialize setter methods using lombok dependency.
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

   @Column
    @ManyToMany(fetch = FetchType.EAGER) //To load a user ans and the same time load all of their roles
    private Collection<UserRole> roles = new ArrayList<>();

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
                   Collection<UserRole> roles) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public AppUser(String firstName, String lastName, String email, String username, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
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