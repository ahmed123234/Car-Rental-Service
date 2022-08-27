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

    String signUpUser(AppUser appUser);

    void enableAppUser(String email);

    Optional<List<AppUser>> getByUserRole(UserRole userRole);

    String getUser(String username, String password);

    List<AppUser> getUsers();

    @Transactional
    void createNewUserAfterOAuthLoginSuccess(String email, String firstName, String lastName, AuthenticationProvider provider);

    void updateUserInfo(String email, AuthenticationProvider provider);

    String  changeStatus(Long userId, boolean status);

    String deleteUser (Long userId);

    void addUser(RegistrationRequest registrationRequest);

    Long getUserId (String username);

    String updateUserPassword(Principal principal, String password);

    String[] getUserRole(Principal principal);

    UserRole saveRole(UserRole role);

    AppUser saveUser(AppUser user);

    UserRole addRoleToUser(String email, String role);
}
