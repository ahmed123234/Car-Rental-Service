package com.example.carrentalservice.services.registration;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final AppUserServiceImpl appUserServiceImpl;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Override
    public String register(RegistrationRequest registrationRequest) {

        String token = appUserServiceImpl.signUpUser(

                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getUserName(),
                        registrationRequest.getPassword()//,
                        //registrationRequest.getRoles()
                ));

//        System.out.println(registrationRequest.getRoles()+ "This is the role");
//        UserDetails user = User.builder()
//                .username(registrationRequest.getUserName())
//                .password(new PasswordEncoder().bCryptPasswordEncoder().encode(registrationRequest.getPassword()))
//                .roles(String.valueOf(registrationRequest.getRoles())) // it will be internally stores as ROLE_(value)
//                .build();
//        new InMemoryUserDetailsManager(user);
//        System.out.println(user);

        return "http://localhost:9090/api/v1/registration/confirm?token=" + token;
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        // search for the user token
        ConfirmationToken confirmationToken = confirmationTokenServiceImpl.getToken(token)
                .orElseThrow(() -> new ApiRequestException("token not found"));

        // check if the email already confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ApiRequestException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiRequestException("token expired");
        }

        confirmationTokenServiceImpl.setConfirmedAt(token);
        appUserServiceImpl.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }
}