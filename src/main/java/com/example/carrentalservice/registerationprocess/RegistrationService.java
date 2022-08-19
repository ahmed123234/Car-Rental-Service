package com.example.carrentalservice.registerationprocess;

import com.example.carrentalservice.AppUser.AppUser;
import com.example.carrentalservice.AppUser.AppUserService;
import com.example.carrentalservice.registerationprocess.regex_validation.EmailValidator;
import com.example.carrentalservice.registerationprocess.regex_validation.PasswordValidator;
import com.example.carrentalservice.registerationprocess.regex_validation.UsernameValidator;
import com.example.carrentalservice.token.ConfirmationToken;
import com.example.carrentalservice.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;
    private final AppUserService appUserService;

    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest registrationRequest) {

        // first we want to check if email, userName and password is valid or not
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());
        boolean isValidPassword = passwordValidator.test(registrationRequest.getPassword());
        boolean isValidUseName = usernameValidator.test(registrationRequest.getUserName());

        if (!isValidEmail) {
            throw new IllegalStateException("The entered email is not valid");
        }

        if (!isValidPassword) {
            throw new IllegalStateException("The entered password is not valid");
        }

        if (!isValidUseName) {
            throw new IllegalStateException("The entered userName is not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getUserName(),
                        registrationRequest.getPassword(),
                        registrationRequest.getRole()
                ));

        return "http://localhost:8080/api/v1/registration/confirm?token=" + token;
    }

    @Transactional
    public String confirmToken(String token) {
        // search for the user token
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        // check if the email already confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        return "confirmed";

    }
}