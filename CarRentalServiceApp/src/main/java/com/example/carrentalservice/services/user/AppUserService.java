package com.example.carrentalservice.services.user;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.models.handelers.RegistrationRequest;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    void checkEmail(String email);

    void checkUsername(String username);

    String signUpUser(AppUser appUser, String[] roles);

    void enableAppUser(String email);

    Optional<List<AppUser>> getByUserRole(String userRole);

    String getUser(String username, String password);

    List<AppUser> getUsers();

    @Transactional
    void createNewUserAfterOAuthLoginSuccess(String email, String firstName, String lastName, AuthenticationProvider provider);

    void updateUserInfo(String email, AuthenticationProvider provider);

    String changeStatus(String username, String status);

    String deleteUser(Long userId);

    String deleteUser (String username);

    void addUser(RegistrationRequest registrationRequest);

    Long getUserId(String username);

    String updateUserPassword(String username, String password);

    String[] getUserRole(String username);

    UserRole saveRole(UserRole role);

    AppUser saveUser(AppUser user);

    UserRole addRoleToUser(String email, String role);

    String handleAuthorizationHeader(String authorizationHeader);

    String getUserOrderCount(String username); // may be better if it removed to order service
}
