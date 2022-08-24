package com.example.carrentalservice.services;

import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import com.example.carrentalservice.models.handelers.regex_validation.RegistrationValidation;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.configuration.authentication.AppUserRole;
import com.example.carrentalservice.configuration.authentication.AuthenticationProvider;
import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.configuration.token.ConfirmationToken;
import com.example.carrentalservice.configuration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final RegistrationValidation registrationValidation;
    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found!";
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws ApiRequestException {

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(()-> new ApiRequestException(String.format(USER_NOT_FOUND_MESSAGE, email)));

        List<GrantedAuthority> grantList = new ArrayList<>();
        if (user.getAppUserRole() != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getAppUserRole().toString().toUpperCase());
            grantList.add(authority);
            System.out.println("inside loadUser method " + authority);

        }

//        return new User(user.getEmail(), user.getPassword(), grantList);
        return user;
    }

    public void checkEmail(String email) {

        boolean emailExists = appUserRepository
                .findByEmail(email)
                .isPresent();

        if (emailExists) {

            throw new ApiRequestException("email already taken");
        }
    }

    public void checkUsername(String username) {

        boolean userNameExists = appUserRepository
                .findByUsername(username)
                .isPresent();

        if (userNameExists) {

            throw new ApiRequestException("userName already taken");
        }

    }

    public String signUpUser(AppUser appUser) {

        checkEmail(appUser.getEmail());

        checkUsername(appUser.getUsername());

        String encodedPassword = passwordEncoder.bCryptPasswordEncoder()
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);


        appUserRepository.save(appUser);

        loadUserByUsername(appUser.getEmail());

        // Generate a random token
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;

    }


    public void enableAppUser(String email) {

        appUserRepository.enableAppUser(email);
    }


    public Optional<List<AppUser>> getByUserRole(AppUserRole appUserRole) {

        boolean isFound = appUserRepository.findByAppUserRole(appUserRole).isPresent();

        if(!isFound){
            throw new ApiRequestException("No such users with the role " + appUserRole);
        }
        return appUserRepository.findByAppUserRole(appUserRole);
    }

    public String getUser(String username, String password) {

        final String USER_NOT_FOUND_MESSAGE = "No such user with the given credentials";

        boolean isFound = appUserRepository.findByUsernameAndPassword(username, password).isPresent();

        if(isFound)

            return "The user with username (" + username + ") and password (" + password + ") is valid";

        throw  new ApiRequestException(String.format(USER_NOT_FOUND_MESSAGE, username));

    }
    @Transactional
    public void CreateNewUserAfterOAuthLoginSuccess(String email, String firstName, String lastName, AuthenticationProvider provider) {

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEnabled(true);
        appUser.setAuthenticationProvider(provider);
        appUser.setAppUserRole(AppUserRole.CUSTOMER);
        appUser.setUsername(lastName + firstName);

        appUserRepository.save(appUser);
    }

    public void updateUserInfo(String email, AuthenticationProvider provider) {

        appUserRepository.updateAppUserInfo(email, provider);
    }


    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }


    public String  changeStatus(Long userId, boolean status) {
        String statusDescription;
        appUserRepository.UpdateStatus(userId, status);
        if(status) {
            statusDescription = "Enabled";
        }
        else statusDescription = "Disabled";
        return "The user with Id " + userId + "is successfully " + statusDescription;
    }


    public String deleteUser (Long userId) {
        appUserRepository.deleteUser(userId);

        return "The user with Id " + userId + "is successfully deleted";
    }


    public void addUser(RegistrationRequest registrationRequest) {

        registrationValidation.validateUserInfo(registrationRequest.getEmail(),
                registrationRequest.getUserName(),
                registrationRequest.getPassword()
                );
        checkEmail(registrationRequest.getEmail());
        checkUsername(registrationRequest.getUserName());
        AppUser appUser = new AppUser(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getUserName(),
                registrationRequest.getPassword(),
                registrationRequest.getRole()
        );

        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }

    public Long getUserId (String username) {

        return appUserRepository.findByUsername(username).get().getUserId();
    }


    public String updateUserPassword(Principal principal, String password) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        appUserRepository.updateUserPassword(loginUser.getUsername(), password);

        return "Password updated successfully";

    }


    public String getUserRole(Principal principal) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) loginUser.getAuthorities();
        return authorities.get(0).getAuthority();

    }
}