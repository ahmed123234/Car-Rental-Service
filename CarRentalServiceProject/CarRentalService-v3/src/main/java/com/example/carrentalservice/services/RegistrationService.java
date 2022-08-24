package com.example.carrentalservice.services;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.configuration.token.ConfirmationToken;
import com.example.carrentalservice.configuration.token.ConfirmationTokenService;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest registrationRequest) {

        String token = appUserService.signUpUser(

                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getUserName(),
                        registrationRequest.getPassword(),
                        registrationRequest.getRole()


                ));
        System.out.println(registrationRequest.getRole()+ "This is the role");
        UserDetails user = User.builder()
                .username(registrationRequest.getUserName())
                .password(new PasswordEncoder().bCryptPasswordEncoder().encode(registrationRequest.getPassword()))
                .roles(String.valueOf(registrationRequest.getRole())) // it will be internally stores as ROLE_(value)
                .build();
        new InMemoryUserDetailsManager(user);
        System.out.println(user);



        return "http://localhost:9090/registration/api/v1/confirm?token=" + token;
    }

    @Transactional
    public String confirmToken(String token) {
        // search for the user token
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new ApiRequestException("token not found"));

        // check if the email already confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ApiRequestException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiRequestException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        return "confirmed";

    }
}