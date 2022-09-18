package com.example.carrentalservice.models.handelers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
//@EqualsAndHashCode
@ToString
public class AppUserRequest {
    @NotBlank(message = "user firstname must not be blank!")
    private final String firstName;

    @NotBlank(message = "user lastname must not be blank!")
    private final String lastName;

    @NotBlank(message = "user email must not be blank!")
    @Pattern(regexp = "^[_A-Za-z\\d-+]+(\\.[_A-Za-z\\d-]+)*@[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$",
            message = "The entered email is not valid")
    private final String email;

    @NotBlank(message = "username must not be blank!")
    @Pattern(regexp = "^[a-zA-Z\\d_-]{8,15}$", message = "The entered username is not valid")
    private final String username;

    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})",
            message = "The entered password is not valid")
    private final String password;

    private final String [] roles;
}

