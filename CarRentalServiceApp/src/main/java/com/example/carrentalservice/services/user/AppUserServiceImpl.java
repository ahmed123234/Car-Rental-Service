package com.example.carrentalservice.services.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.models.handelers.RegistrationRequest;

import com.example.carrentalservice.models.handelers.regex_validation.RegistrationValidation;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import com.example.carrentalservice.repositories.UserRoleRepository;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements UserDetailsService, AppUserService {

    private final UserRoleRepository userRoleRepository;
    private final RegistrationValidation registrationValidation;
    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found!";
    final String CREDENTIALS_ERROR_MESSAGE = "No such user with the given credentials %s";

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws ApiRequestException {

        AppUser user = appUserRepository.findByEmail(username);

        if (user == null) {
            log.error("User with email {} not found in the database", username);
            throw new ApiRequestException(String.format(USER_NOT_FOUND_MESSAGE, username));
        }
        log.info("User with email {} found in the database", username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    public void checkEmail(String email) {

        AppUser user = appUserRepository.findByEmail(email);

        if (user != null) {
            log.error("Email: {} already taken", email);
            throw new ApiRequestException("email already taken");
        }else
            log.info("Email: {} is unique and seem good", email);
    }

    @Override
    public void checkUsername(String username) {

        boolean userNameExists = appUserRepository.findByUsername(username).isPresent();

        if (userNameExists) {
            log.error("username: {} already taken", username);
            throw new ApiRequestException("username already taken");
        }else
            log.info("username: {} is unique and seem good", username);
    }

    @Override
    public String signUpUser(AppUser appUser, String[] roles) {

        checkEmail(appUser.getEmail());
        checkUsername(appUser.getUsername());

        saveUser(appUser);

        for (String role: roles
        ) {
            appUser.getRoles().add(addRoleToUser(appUser.getEmail(), role));
        }


        // Generate a random token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenServiceImpl.saveConfirmationToken(confirmationToken);

        log.info("Send the token: {} to user with email {} to confirm it", token, appUser.getEmail());
        return token;
    }

    @Override
    public void enableAppUser(String email) {

       if (appUserRepository.enableAppUser(email) == 1)
           log.info("Email: {} is enabled successfully", email);
       else
           log.error("Error enabling Email: {} ", email);
    }

    @Override
    public Optional<List<AppUser>> getByUserRole(String userRole) {

        UserRole role = userRoleRepository.findByName(userRole);
        boolean isFound = appUserRepository.findByRoles(role).isPresent();

        if(!isFound){
            log.info("No such users with role {} ", userRole);
            throw new ApiRequestException("No such users with the role " + userRole);
        }
        log.info("There is user/s with role {} ", userRole);
        return appUserRepository.findByRoles(role);
    }
    @Override
    public String getUser(String username, String password) {
        String encodedPassword = appUserRepository.getEncodedPassword(username);

        boolean isPasswordMatch = passwordEncoder.bCryptPasswordEncoder().matches(password, encodedPassword);

        if(isPasswordMatch) {
            log.info("The user: {} is a valid user", username);
            return "The user with username :" + username + " and password: " + password + " is valid";
        }
        log.error( CREDENTIALS_ERROR_MESSAGE + "{} & {}", username, password);
        throw  new ApiRequestException(String.format(CREDENTIALS_ERROR_MESSAGE, username));
    }

    @Override
    public String getUserOrderCount(String username) {

        boolean isFound = appUserRepository.findByUsername(username).isPresent();

        if(isFound) {
            log.info("The user: {} is a valid user", username);
            return "user: "+ username + " has " + appUserRepository.getUserOrdersCount(username) +
                    " orders till present";
        }
        log.error("no such user with name {} found", username);
        throw  new ApiRequestException(String.format("no such user with name: " + username + " found"));
    }

    @Override
    @Transactional
    public void createNewUserAfterOAuthLoginSuccess(String email, String firstName,
                                                    String lastName, AuthenticationProvider provider) {

        //List<UserRole> roles = new ArrayList<>();
        //roles.add(new UserRole(null, "ROLE_CUSTOMER"));
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEnabled(true);
        appUser.setAuthenticationProvider(provider);
        //appUser.setRoles(roles);
        appUser.setUsername(lastName + firstName);
        appUserRepository.save(appUser);
        appUser.getRoles().add(addRoleToUser(email, "ROLE_CUSTOMER"));
        log.info("the user {} signup using google, and saved in database successfully", appUser.getUsername());

    }
    @Override
    public void updateUserInfo(String email, AuthenticationProvider provider) {
        if (appUserRepository.findByEmail(email) != null) {
            appUserRepository.updateAppUserInfo(email, provider);
            log.info("the user with email {}, logged in by google, " +
                    "and his authentication provider is changed to {}", email, provider);
        }else {
            log.error("No such user with email : {}", email);
            throw new ApiRequestException("No such user with email: " + email);
        }
    }

    @Override
    public List<AppUser> getUsers() {

        log.info("list all users ");
        return appUserRepository.findAll();
    }

    @Override
    public String  changeStatus(String username, String status) {
        String statusDescription;
        AppUser user = appUserRepository.findAppUserByUsername(username);

        if (user != null) {
            log.info("user with name: {} is valid , " +
                            "the changing status process is in progress",  username);
        }
        else {
            log.info("no such user with name: {} " +
                    " the changing status process is failed", username);
            throw  new ApiRequestException("no such user with username: " + username +
                    " the changing status process is failed");
        }
        boolean state = status.equalsIgnoreCase("enabled");
        appUserRepository.UpdateStatus(username, state);

        if (state) {
                statusDescription = "Enabled";
        } else statusDescription = "Disabled";
        log.info("User {} is {} successfully", username, statusDescription);
        return "The user with username " + username + " is successfully " + statusDescription;
    }


    @Override
    public String deleteUser (String username) {

        int isAffected = appUserRepository.deleteUser(username);
        if(isAffected == 0) {
            log.error("no such user with username : {}, the deletion is ignored.", username);
            throw new ApiRequestException(" The user with username: " + username + "not found in the database.\n" +
                    "deletion is ignored");
        }
        log.info("user with username: {} is deleted successfully", username);
        return "The user with username: " + username + " is successfully deleted";
    }

    @Override
    public String deleteUser (Long userId) {

        int isAffected = appUserRepository.deleteUserByID(userId);
        if(isAffected == 0) {
            log.error("ni such user with id : {}, the deletion is ignored.", userId);
            throw new ApiRequestException(" The user with id: " + userId + "not found in the database.\n" +
                    "deletion is ignored");
        }
        log.info("user with id: {} is deleted successfully", userId);
        return "The user with Id " + userId + "is successfully deleted";
    }

    @Override
    public void addUser(RegistrationRequest registrationRequest) {

        registrationValidation.validateUserInfo(registrationRequest.getEmail(),
                registrationRequest.getUserName(), registrationRequest.getPassword());

        checkEmail(registrationRequest.getEmail());
        checkUsername(registrationRequest.getUserName());
        AppUser appUser = new AppUser(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getUserName(),
                registrationRequest.getPassword()
        );

        appUser.setEnabled(true);
        saveUser(appUser);
        for (String role: registrationRequest.getRoles()
        ) {
            addRoleToUser(registrationRequest.getEmail(), role);
        }

    }
    @Override
    public Long getUserId (String username) {

        boolean isFound = appUserRepository.findByUsername(username).isPresent();
        if (!isFound) {
            log.error("User  with username: {} not found in the database", username);
            return null;
        }
        return appUserRepository.findByUsername(username).get().getUserId();
    }

    @Override
    public String updateUserPassword(@NotNull String username, @NotNull String password) {

        registrationValidation.validateUserInfo(null, username, password);
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);

        System.out.println("new password is " + password);
        int affected = appUserRepository.updateUserPassword(username, encodedPassword);

        if (affected == 1) {
            log.info("Update password to user: {} to be {}", username, password);
            return "Password updated successfully";
        }else {
            log.error("error updating password for user {}", username);
            return "Password updating failed";
        }
    }


    @Override
    public String[] getUserRole(String username) {

        AppUser user = appUserRepository.findAppUserByUsername(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getAuthority())));
        System.out.println(authorities.get(0).getAuthority());

        String [] roles = new String[authorities.size()];

        for (int i =0; i < authorities.size(); i++) {

            roles[i] = authorities.get(i).getAuthority();
            System.out.println(roles[i]);
        }

        System.out.println(Arrays.toString(roles));
        return roles;
    }

    @Override
    public UserRole saveRole(UserRole role) {
        log.info("saving new role {} to the database", role.getName());
        return userRoleRepository.save(role);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public UserRole addRoleToUser(String email, String roleName) {
        AppUser user = appUserRepository.findByEmail(email);

        if (user == null) {
            log.error("No such user with email:{} ", email);
            throw new ApiRequestException("No such user with email: " + email);
        }

        UserRole role = userRoleRepository.findByName(roleName);

        if (role == null) {
            log.error("No such role named:{} ", roleName);
            throw new ApiRequestException("No such role named: " + roleName);
        }
        user.getRoles().add(role);
        log.info("Adding role {} to user {}", roleName, email);
        return  role;
    }

    @Override
    public String handleAuthorizationHeader(@NotNull String authorizationHeader) {

        String accessToken = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();
        System.out.println(username + "  +++++ this is username");
        return username;
    }
}