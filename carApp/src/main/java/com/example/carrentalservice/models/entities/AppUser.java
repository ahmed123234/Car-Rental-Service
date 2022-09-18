package com.example.carrentalservice.models.entities;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @NotBlank(message = "user firstname must not be blank!")
    private String firstName;
    @Column(nullable = false)
    @NotBlank(message = "user lastname must not be blank!")
    private String lastName;
    @Email
    @Pattern(regexp = "^[_A-Za-z\\d-+]+(\\.[_A-Za-z\\d-]+)*@[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$",
            message = "The entered email is not valid")
    @NotBlank(message = "user email must not be blank!")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "username must not be blank!")
    @Pattern(regexp = "^[a-zA-Z\\d_-]{8,15}$", message = "The entered username is not valid")
    private String username;

    @Column
//    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})",
//    message = "The entered password is not valid")
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

    public void addRole(UserRole role) {
        this.roles.add(role);
    }
}